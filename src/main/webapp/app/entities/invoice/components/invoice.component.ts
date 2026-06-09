// import { Component, OnInit, OnDestroy } from '@angular/core';
// import { ActivatedRoute } from '@angular/router';
// import { IVoucher } from 'app/entities/voucher/voucher.model';
// import { CustomerService } from 'app/entities/customer/service/customer.service';
// import { AurumServiceService } from 'app/entities/aurum-service/service/aurum-service.service';
// import { ICustomer } from 'app/entities/customer/customer.model';
// import { IAurumService } from 'app/entities/aurum-service/aurum-service.model';
// import { VoucherService } from 'app/entities/voucher/service/voucher.service';
// import { TransactionHistoryService } from 'app/entities/transaction-history/service/transaction-history.service';
// import { ITransactionHistory } from 'app/entities/transaction-history/transaction-history.model';
// import { AmountInWords } from '../util/amount.in.words';
// import { RateService } from 'app/entities/rate/service/rate.service';

// export class TypedAurumService {
//   serviceType: string;
//   serviceList: IAurumService[];
// }

// @Component({
  standalone: false,
//   selector: 'jhi-aurum-invoice',
//   templateUrl: './invoice.component.html',
//   styleUrls: ['./invoice.component.scss']
// })
// export class InvoiceComponent implements OnInit, OnDestroy {
//   voucherNumber: string;
//   voucher: IVoucher;
//   customer: ICustomer;
//   aurumServices: IAurumService[];
//   typedAurumServiceList: TypedAurumService[];
//   serviceTypeToServiceListMap: Map<string, IAurumService[]>;
//   txnHitoryList: ITransactionHistory[];
//   paidAmount: number;
//   distinctServiceType: string[];
//   invoiceTitle: string;
//   printDate: Date = new Date();
//   rateTypePriceMap: Map<string, number> = new Map();

//   showXrayNote: boolean;
//   showMeltingNote: boolean;
//   showCalculatedMeltingNote: boolean;

//   amountInWordsStr: string;

//   constructor(
//     private route: ActivatedRoute,
//     protected customerService: CustomerService,
//     protected aurumServiceService: AurumServiceService,
//     protected voucherService: VoucherService,
//     protected transactionHistoryService: TransactionHistoryService,
//     private amountInWordsService: AmountInWords,
//     protected rateService: RateService
//   ) {}

//   ngOnInit() {
//     this.route.params.subscribe(param => {
//       this.voucherNumber = param['id'];
//       this.fetchVoucherInformation(this.voucherNumber);
//     });
//     this.fetchRateList();
//   }

//   fetchVoucherInformation(voucherNo: string) {
//     this.voucherService.query({ 'voucherNo.equals': voucherNo }).subscribe(data => {
//       this.voucher = data.body[0];

//       this.fetchCustomer(this.voucher.customerId);
//       this.fetchAurumServices(this.voucher.id);
//       this.fetchTxnHistory(this.voucher.voucherNo);

//       this.amountInWordsStr = this.amountInWordsService.convertNumberToWords(this.voucher.totalPayableAmount);
//     });
//   }

//   fetchCustomer(id: number) {
//     this.customerService.find(id).subscribe(data => {
//       this.customer = data.body;
//     });
//   }

//   fetchAurumServices(voucherId: number) {
//     this.distinctServiceType = [];
//     this.serviceTypeToServiceListMap = new Map();
//     this.aurumServiceService.query({ 'voucherId.equals': voucherId }).subscribe(data => {
//       this.aurumServices = data.body;

//       this.aurumServices.map(as => {
//         if (as.serviceType === 'X-Ray') this.showXrayNote = true;
//         if (as.serviceType === 'Normal Melting' || as.serviceType === 'Calculated Melting') this.showMeltingNote = true;
//         if (as.serviceType === 'Calculated Melting') this.showCalculatedMeltingNote = true;
//         if (!this.distinctServiceType.includes(as.serviceType)) this.distinctServiceType.push(as.serviceType);
//         if (!this.serviceTypeToServiceListMap.has(as.serviceType)) this.serviceTypeToServiceListMap.set(as.serviceType, []);
//       });
//       this.invoiceTitle = this.distinctServiceType.join(', ');

//       this.aurumServices.map(service => {
//         this.serviceTypeToServiceListMap.get(service.serviceType).push(service);
//       });
//     });
//   }

//   fetchTxnHistory(voucherNo: string) {
//     this.paidAmount = 0;
//     this.transactionHistoryService.query({ 'voucherNo.equals': voucherNo }).subscribe(data => {
//       this.txnHitoryList = data.body;
//       this.txnHitoryList.map(txn => {
//         if (txn.tag === 'RECEIVE') this.paidAmount += txn.amount;
//       });
//       // this.amountInWordsStr = this.amountInWordsService.convertNumberToWords(this.paidAmount);
//     });
//   }

//   fetchRateList() {
//     this.rateService.query().subscribe(data => {
//       if (data.body && data.body.length !== 0) {
//         data.body.map(rate => {
//           this.rateTypePriceMap.set(rate.rateType, rate.unitPrice);
//         });
//       }
//     });
//   }

//   printPage() {
//     window.print();
//   }

//   ngOnDestroy() {}
// }
