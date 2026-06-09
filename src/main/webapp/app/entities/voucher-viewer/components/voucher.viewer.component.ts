import { Component, OnInit, OnDestroy } from '@angular/core';
import { AlertService } from 'app/core/util/alert.service';
import moment from 'moment';
import { VoucherViewerService } from '../services/service-api/voucher.viewer.service';
import { VoucherViewer, TransactionDto } from '../services/domain/voucher.viewer.models';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { FormGroup, Validators, FormBuilder } from '@angular/forms';
import { ITransactionHistory } from 'app/entities/transaction-history/transaction-history.model';
import { TransactionHistoryService } from 'app/entities/transaction-history/service/transaction-history.service';
import { TransactionStatus } from 'app/entities/enumerations/transaction-status.model';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { AurumServiceService } from 'app/entities/aurum-service/service/aurum-service.service';
import { IAurumService } from 'app/entities/aurum-service/aurum-service.model';
import { VoucherService } from 'app/entities/voucher/service/voucher.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { VoucherStatus } from 'app/entities/enumerations/voucher-status.model';

@Component({
  selector: 'jhi-aurum-voucher-viewer',
  templateUrl: './voucher.viewer.component.html',
  styleUrls: ['./voucher.viewer.component.scss']
})
export class VoucherViewerComponent implements OnInit, OnDestroy {
  account: Account;
  customerName: string;
  voucherFieldValue: string;
  voucherNumber: string;
  voucherViewer = new VoucherViewer();
  aurumServices: IAurumService[] = [];

  transactionHistoryForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    protected jhiAlertService: AlertService,
    protected transactionHistoryService: TransactionHistoryService,
    protected customerService: CustomerService,
    protected aurumServiceService: AurumServiceService,
    protected voucherService: VoucherService,
    private voucherViewerService: VoucherViewerService,
    private accountService: AccountService,
    private modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.prepareTransactionHistoryForm();

    this.accountService.identity().subscribe((account: Account) => {
      this.account = account;
    });
  }

  searchVoucherViewer() {
    this.voucherViewer = new VoucherViewer();
    if (this.voucherFieldValue) {
      this.voucherViewerService.find(this.voucherFieldValue).subscribe(
        data => {
          if (!this.voucherViewer) {
            this.jhiAlertService.addAlert({ type: 'warning', message: 'Voucher not found.' });
          } else {
            this.voucherViewer = data.body;
            this.voucherNumber = this.voucherViewer.voucherInfo.voucherNo;
            this.transactionHistoryForm.controls.amount.setValidators([Validators.required, Validators.max(this.voucherViewer.dueAmount)]);
            this.transactionHistoryForm.controls.amount.updateValueAndValidity();

            this.fetchCustomer(this.voucherViewer.voucherInfo.customerId);
            this.fetchAurumServices(this.voucherViewer.voucherInfo.id);
          }
        },
        error => {
          this.voucherViewer = null;
          this.voucherNumber = null;
          this.customerName = null;
          this.aurumServices = [];
          this.jhiAlertService.addAlert({ type: 'warning', message: 'Voucher not found.' });
        }
      );
    } else {
      this.jhiAlertService.addAlert({ type: 'info', message: 'Please provide voucher number.' });
    }
  }

  fetchCustomer(id: number) {
    this.customerService.find(id).subscribe(data => {
      this.customerName = data.body.firstName + ' ' + (data.body.lastName ? data.body.lastName : '');
    });
  }

  fetchAurumServices(voucherId: number) {
    this.aurumServiceService.query({ 'voucherId.equals': voucherId }).subscribe(data => {
      this.aurumServices = data.body;
    });
  }

  prepareTransactionHistoryForm() {
    this.transactionHistoryForm = this.formBuilder.group({
      amount: ['', [Validators.required]]
    });
  }

  confirmMakePayment(confirmPaymentDialog, confirmPaymentWithDeliveryDialog) {
    this.markFormGroupAsTouched(this.transactionHistoryForm);
    if (!this.transactionHistoryForm.controls.amount.value) {
      this.jhiAlertService.addAlert({ type: 'warning', message: 'Amount not found.' });
      return;
    }
    if (this.transactionHistoryForm.invalid) {
      this.jhiAlertService.addAlert({ type: 'warning', message: "Amount can't be greater then due." });
      return;
    }

    if (+(+this.transactionHistoryForm.controls.amount.value).toFixed(2) < +this.voucherViewer.dueAmount.toFixed(2)) {
      this.modalService.open(confirmPaymentDialog, { centered: true }); // TODO:
    } else if (
      +(+this.transactionHistoryForm.controls.amount.value).toFixed(2) === +this.voucherViewer.dueAmount.toFixed(2) &&
      !this.voucherViewer.voucherInfo.deliveryStatus
    )
      this.modalService.open(confirmPaymentWithDeliveryDialog, { centered: true });
    else this.makePayment(false, false);
  }

  makePayment(deliveryStatus: boolean, isPaid: boolean) {
    const transactionHistoryTemp: any = {};
    transactionHistoryTemp.voucherNo = this.voucherNumber;
    transactionHistoryTemp.amount = this.transactionHistoryForm.controls.amount.value;
    transactionHistoryTemp.tag = TransactionStatus.RECEIVE;
    transactionHistoryTemp.customerId = this.voucherViewer.voucherInfo.customerId;
    transactionHistoryTemp.dateCreated = moment(new Date(), DATE_TIME_FORMAT);
    transactionHistoryTemp.addedBy = this.account.login;

    const transactionDto = new TransactionDto();
    transactionDto.transactionHistory = transactionHistoryTemp;
    transactionDto.deliveryStatus = deliveryStatus;
    transactionDto.voucherStatus = isPaid ? VoucherStatus.PAID : VoucherStatus.DUE;

    this.voucherViewerService.createCustomeTransaction(transactionDto).subscribe(txn => {
      // search voucher data again
      this.voucherViewerService.find(this.voucherFieldValue).subscribe(voucher => {
        if (this.voucherViewer) {
          this.voucherViewer = voucher.body;
          this.voucherNumber = this.voucherViewer.voucherInfo.voucherNo;
          this.transactionHistoryForm.controls.amount.setValidators([Validators.max(this.voucherViewer.dueAmount)]);
          this.transactionHistoryForm.controls.amount.updateValueAndValidity();

          this.fetchCustomer(this.voucherViewer.voucherInfo.customerId);
          this.fetchAurumServices(this.voucherViewer.voucherInfo.id);
        }
      });
      this.transactionHistoryForm.controls.amount.setValue(null);
    });
  }

  confirmDelivery(confirmDeliveryDialog) {
    this.modalService.open(confirmDeliveryDialog, { centered: true });
  }

  deleteVoucherDialog(confirmDeleteVoucherDialog) {
    this.modalService.open(confirmDeleteVoucherDialog, { centered: true });
  }

  deleteVoucher() {
    if (this.voucherFieldValue) {
      this.voucherViewerService.deleteVoucher(this.voucherFieldValue).subscribe(data => {
        this.voucherViewer = new VoucherViewer();
        this.voucherFieldValue = null;
        this.jhiAlertService.addAlert({ type: 'success', message: 'Voucher deleted successfully.' });
      });
    }
  }

  makeDelivery() {
    const voucher = this.voucherViewer.voucherInfo;
    voucher.deliveryStatus = true;

    this.voucherService.update(voucher).subscribe(data => {
      this.jhiAlertService.addAlert({ type: 'success', message: 'Delivery Completed.' });
    });
  }

  ngOnDestroy(): void {}

  // Util methods
  numberOnly(event): boolean {
    const charCode = event.which ? event.which : event.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
      return false;
    }
    return true;
  }

  markFormGroupAsTouched(formGroup: FormGroup) {
    (Object as any).values(formGroup.controls).forEach(control => {
      control.markAsDirty();
      control.markAllAsTouched();
      if (control.controls) {
        control.controls.forEach(ctrl => this.markFormGroupAsTouched(ctrl));
      }
    });
  }
}
