import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Voucher } from 'app/shared/model/voucher.model';
import { CustomerService } from 'app/entities/customer/customer.service';
import { AurumServiceService } from 'app/entities/aurum-service/aurum-service.service';
import { Customer } from 'app/shared/model/customer.model';
import { AurumService } from 'app/shared/model/aurum-service.model';
import { VoucherService } from 'app/entities/voucher/voucher.service';
import { TransactionHistoryService } from 'app/entities/transaction-history/transaction-history.service';
import { TransactionHistory } from 'app/shared/model/transaction-history.model';

@Component({
  selector: 'jhi-aurum-invoice',
  templateUrl: './invoice.component.html',
  styleUrls: ['./invoice.component.scss']
})
export class InvoiceComponent implements OnInit, OnDestroy {
  voucherNumber: string;
  voucher: Voucher;
  customer: Customer;
  aurumServices: AurumService[];
  txnHitoryList: TransactionHistory[];
  paidAmount: number;
  distinctServiceType: string[];
  invoiceTitle: string;
  printDate: Date = new Date();

  constructor(
    private route: ActivatedRoute,
    protected customerService: CustomerService,
    protected aurumServiceService: AurumServiceService,
    protected voucherService: VoucherService,
    protected transactionHistoryService: TransactionHistoryService
  ) {}

  ngOnInit() {
    this.route.params.subscribe(param => {
      this.voucherNumber = param['id'];
      this.fetchVoucherInformation(this.voucherNumber);
    });
  }

  fetchVoucherInformation(voucherNo: string) {
    this.voucherService.query({ 'voucherNo.equals': voucherNo }).subscribe(data => {
      this.voucher = data.body[0];

      this.fetchCustomer(this.voucher.customerId);
      this.fetchAurumServices(this.voucher.id);
      this.fetchTxnHistory(this.voucher.voucherNo);
    });
  }

  fetchCustomer(id: number) {
    this.customerService.find(id).subscribe(data => {
      this.customer = data.body;
    });
  }

  fetchAurumServices(voucherId: number) {
    this.distinctServiceType = [];
    this.aurumServiceService.query({ 'voucherId.equals': voucherId }).subscribe(data => {
      this.aurumServices = data.body;
      this.aurumServices.map(as => {
        if (!this.distinctServiceType.includes(as.serviceType)) this.distinctServiceType.push(as.serviceType);
        this.invoiceTitle = this.distinctServiceType.join(', ');
      });
    });
  }

  fetchTxnHistory(voucherNo: string) {
    this.paidAmount = 0;
    this.transactionHistoryService.query({ 'voucherNo.equals': voucherNo }).subscribe(data => {
      this.txnHitoryList = data.body;
      this.txnHitoryList.map(txn => {
        if (txn.tag === 'RECEIVE') this.paidAmount += txn.amount;
      });
    });
  }

  ngOnDestroy() {}
}
