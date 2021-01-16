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
import { AmountInWords } from '../util/amount.in.words';
import { RateService } from 'app/entities/rate/rate.service';

export class TypedAurumService {
  serviceType: string;
  serviceList: AurumService[];
}

@Component({
  selector: 'jhi-aurum-invoice-new',
  templateUrl: './invoice.component.html',
  styleUrls: ['./invoice.component.scss']
})
export class InvoiceNewComponent implements OnInit, OnDestroy {
  voucherNumber: string;
  voucher: Voucher;
  customer: Customer;
  aurumServices: AurumService[];
  typedAurumServiceList: TypedAurumService[];
  serviceTypeToServiceListMap: Map<string, AurumService[]>;
  txnHitoryList: TransactionHistory[];
  paidAmount: number;
  distinctServiceType: string[];
  invoiceTitle: string;
  printDate: Date = new Date();
  rateTypePriceMap: Map<string, number> = new Map();

  showXrayNote: boolean;
  showMeltingNote: boolean;
  showCalculatedMeltingNote: boolean;

  isPrintFirstSection: boolean;

  amountInWordsStr: string;

  constructor(
    private route: ActivatedRoute,
    protected customerService: CustomerService,
    protected aurumServiceService: AurumServiceService,
    protected voucherService: VoucherService,
    protected transactionHistoryService: TransactionHistoryService,
    private amountInWordsService: AmountInWords,
    protected rateService: RateService
  ) {}

  ngOnInit() {
    this.route.params.subscribe(param => {
      this.voucherNumber = param['id'];
      this.fetchVoucherInformation(this.voucherNumber);
    });
    this.fetchRateList();
  }

  fetchVoucherInformation(voucherNo: string) {
    this.voucherService.query({ 'voucherNo.equals': voucherNo }).subscribe(data => {
      this.voucher = data.body[0];

      this.fetchCustomer(this.voucher.customerId);
      this.fetchAurumServices(this.voucher.id);
      this.fetchTxnHistory(this.voucher.voucherNo);

      this.amountInWordsStr = this.amountInWordsService.convertNumberToWords(this.voucher.totalPayableAmount);
    });
  }

  fetchCustomer(id: number) {
    this.customerService.find(id).subscribe(data => {
      this.customer = data.body;
    });
  }

  fetchAurumServices(voucherId: number) {
    this.distinctServiceType = [];
    this.serviceTypeToServiceListMap = new Map();
    this.aurumServiceService.query({ 'voucherId.equals': voucherId }).subscribe(data => {
      this.aurumServices = data.body;

      this.aurumServices.map(as => {
        if (as.serviceType === 'X-Ray') this.showXrayNote = true;
        if (as.serviceType === 'Normal Melting' || as.serviceType === 'Calculated Melting') this.showMeltingNote = true;
        if (as.serviceType === 'Calculated Melting') this.showCalculatedMeltingNote = true;
        if (!this.distinctServiceType.includes(as.serviceType)) this.distinctServiceType.push(as.serviceType);
        if (!this.serviceTypeToServiceListMap.has(as.serviceType)) this.serviceTypeToServiceListMap.set(as.serviceType, []);
      });
      this.invoiceTitle = this.distinctServiceType.join(', ');

      this.aurumServices.map(service => {
        this.serviceTypeToServiceListMap.get(service.serviceType).push(service);
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
      // this.amountInWordsStr = this.amountInWordsService.convertNumberToWords(this.paidAmount);
    });
  }

  fetchRateList() {
    this.rateService.query().subscribe(data => {
      if (data.body && data.body.length !== 0) {
        data.body.map(rate => {
          this.rateTypePriceMap.set(rate.rateType, rate.unitPrice);
        });
      }
    });
  }

  printPage() {
    this.isPrintFirstSection = true;
    window.print();
  }

  printForCustomer() {
    // this.isPrintFirstSection = false;
    // setTimeout(() => {
    //   window.print();
    // }, 100);

    // const printContent = document.getElementById("customerPrintSection");
    const printContent = document.getElementById('customerPrintSection').innerHTML;
    const WindowPrt = window.open('', '', 'left=0,top=0,width=900,height=900,toolbar=0,scrollbars=0,status=0');
    WindowPrt.document.open();
    WindowPrt.document.write(`
        <html>
         <style>
            body{
                font-family: 'Times new roman'
            }
            .printButton{
              background-color: #009688;
              color: #ffffff;
              border: none;
              width: 80px;
              height: 40px;
              float:right;
            }
            @media print {
              .printButton{
                display: none;
              }

              .container,
              .container-fluid,
              .container-sm,
              .container-md,
              .container-lg,
              .container-xl {
                width: 100%;
                padding-right: 15px;
                padding-left: 15px;
                margin-right: auto;
                margin-left: auto;
              }

              @media (min-width: 768px) {
                .col-md-6 {
                  -ms-flex: 0 0 46%;
                  flex: 0 0 46%;
                  max-width: 50%;
                }
                .col-md-12 {
                  -ms-flex: 0 0 96%;
                  flex: 0 0 96%;
                  max-width: 100%;
                }
              }

              .row {
                display: -ms-flexbox;
                display: flex;
                -ms-flex-wrap: wrap;
                flex-wrap: wrap;
                margin-right: -15px;
                margin-left: -15px;
              }

              .col-md-6, .col-md-12, 
              .col-md {
                position: relative;
                width: 100%;
                padding-right: 15px;
                padding-left: 15px;
              }

              .table {
                width: 100%;
                margin-bottom: 1rem;
                color: #212529;
              }

              .table-borderless th,
              .table-borderless td,
              .table-borderless thead th,
              .table-borderless tbody + tbody {
                border: 0;
              }

              .cusBorderStyle {
                border: 1px solid black;
                text-align: left;
                vertical-align: middle;
              }
            }
            .container,
            .container-fluid,
            .container-sm,
            .container-md,
            .container-lg,
            .container-xl {
              width: 100%;
              padding-right: 15px;
              padding-left: 15px;
              margin-right: auto;
              margin-left: auto;
            }

            @media (min-width: 768px) {
              .col-md-6 {
                -ms-flex: 0 0 46%;
                flex: 0 0 46%;
                max-width: 50%;
              }
              .col-md-12 {
                -ms-flex: 0 0 96%;
                flex: 0 0 96%;
                max-width: 100%;
              }
            }

            .row {
              display: -ms-flexbox;
              display: flex;
              -ms-flex-wrap: wrap;
              flex-wrap: wrap;
              margin-right: -15px;
              margin-left: -15px;
            }

            .col-md-6, .col-md-12, 
            .col-md {
              position: relative;
              width: 100%;
              padding-right: 15px;
              padding-left: 15px;
            }

            table {
              border-collapse: collapse;
            }

            .table {
              width: 100%;
              margin-bottom: 1rem;
              color: #212529;
            }

            .table th,
            .table td {
              padding: 5px;
            }

            .table-bordered {
              border: 1px solid black;
            }

            .table-bordered th,
            .table-bordered td {
              border: 1px solid black;
            }

            .table-bordered thead th,
            .table-bordered thead td {
              border-bottom-width: 2px;
            }

            .table-borderless th,
            .table-borderless td,
            .table-borderless thead th,
            .table-borderless tbody + tbody {
              border: 0;
            }

            .cusBorderStyle {
              border: 1px solid black;
              text-align: left;
              vertical-align: middle;
            }
          }

         </style> 
        </head>
      <body>

        <button onClick="window.print();window.close()" class="printButton"> Print</button>
      
        <div>
            ${printContent}
        </div>
      </body>
    </html>`);
    WindowPrt.document.close();
    WindowPrt.focus();
    // WindowPrt.print();
    // WindowPrt.close();
  }

  ngOnDestroy() {}
}
