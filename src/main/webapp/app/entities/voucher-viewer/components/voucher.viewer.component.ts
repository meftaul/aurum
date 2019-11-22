import { Component, OnInit, OnDestroy } from '@angular/core';
import { JhiAlertService } from 'ng-jhipster';
import * as moment from 'moment';
import { VoucherViewerService } from '../services/service-api/voucher.viewer.service';
import { VoucherViewer } from '../services/domain/voucher.viewer.models';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
import { FormGroup, Validators, FormBuilder } from '@angular/forms';
import { TransactionHistory } from 'app/shared/model/transaction-history.model';
import { TransactionHistoryService } from 'app/entities/transaction-history/transaction-history.service';
import { TransactionStatus } from 'app/shared/model/enumerations/transaction-status.model';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { CustomerService } from 'app/entities/customer/customer.service';
import { AurumServiceService } from 'app/entities/aurum-service/aurum-service.service';
import { AurumService } from 'app/shared/model/aurum-service.model';

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
  aurumServices: AurumService[] = [];

  transactionHistoryForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    protected jhiAlertService: JhiAlertService,
    protected transactionHistoryService: TransactionHistoryService,
    protected customerService: CustomerService,
    protected aurumServiceService: AurumServiceService,
    private voucherViewerService: VoucherViewerService,
    private accountService: AccountService
  ) {}

  ngOnInit(): void {
    this.prepareTransactionHistoryForm();

    this.accountService.identity().then((account: Account) => {
      this.account = account;
    });
  }

  searchVoucherViewer() {
    if (this.voucherFieldValue) {
      this.voucherViewerService.find(this.voucherFieldValue).subscribe(
        data => {
          if (!this.voucherViewer) {
            this.jhiAlertService.warning('Voucher not found.');
          } else {
            this.voucherViewer = data.body;
            this.voucherNumber = this.voucherViewer.voucherInfo.voucherNo;
            this.transactionHistoryForm.controls.amount.setValidators([Validators.max(this.voucherViewer.dueAmount)]);
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
          this.jhiAlertService.warning('Voucher not found.');
        }
      );
    } else {
      this.jhiAlertService.info('Please provide voucher number.');
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
      // itemName: ['', [Validators.required]],
    });
  }

  makePayment() {
    this.transactionHistoryForm.markAllAsTouched();
    if (this.transactionHistoryForm.invalid) {
      this.jhiAlertService.warning("Amount can't be greater then due.");
      return;
    }
    // call service to save transaction history
    const transactionHistoryTemp = new TransactionHistory();
    transactionHistoryTemp.voucherNo = this.voucherNumber;
    transactionHistoryTemp.amount = this.transactionHistoryForm.controls.amount.value;
    transactionHistoryTemp.tag = TransactionStatus.RECEIVE;
    transactionHistoryTemp.customerId = this.voucherViewer.voucherInfo.customerId;
    transactionHistoryTemp.dateCreated = moment(new Date(), DATE_TIME_FORMAT);
    transactionHistoryTemp.addedBy = this.account.login;

    this.transactionHistoryService.create(transactionHistoryTemp).subscribe(transaction => {
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

  ngOnDestroy(): void {}

  // Util methods
  numberOnly(event): boolean {
    const charCode = event.which ? event.which : event.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
      return false;
    }
    return true;
  }
}
