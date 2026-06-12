import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { IVoucher } from 'app/entities/voucher/voucher.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { AurumServiceService } from 'app/entities/aurum-service/service/aurum-service.service';
import { ICustomer } from 'app/entities/customer/customer.model';
import { IAurumService } from 'app/entities/aurum-service/aurum-service.model';
import { VoucherService } from 'app/entities/voucher/service/voucher.service';
import { TransactionHistoryService } from 'app/entities/transaction-history/service/transaction-history.service';
import { ITransactionHistory } from 'app/entities/transaction-history/transaction-history.model';
import { AmountInWords } from '../util/amount.in.words';
import { RateService } from 'app/entities/rate/service/rate.service';
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';

export class TypedAurumService {
  serviceType: string;
  serviceList: IAurumService[];
}

/**
 * Shop header printed at the top of the 78mm POS receipt.
 * Single source of truth — edit these lines to change the receipt header.
 * NOTE: address/phone are placeholders awaiting the exact text from the shop.
 */
export const SHOP_HEADER = {
  name: 'Narayanganj Gold',
  addressLines: ['<<ADDRESS LINE — please fill in>>'],
  phone: '<<PHONE — please fill in>>',
};

@Component({
  standalone: false,
  selector: 'jhi-aurum-invoice-new',
  templateUrl: './invoice.component.html',
  styleUrls: ['./invoice.component.scss'],
})
export class InvoiceNewComponent implements OnInit, OnDestroy {
  voucherNumber: string;
  voucher: IVoucher;
  customer: ICustomer;
  aurumServices: IAurumService[];
  typedAurumServiceList: TypedAurumService[];
  serviceTypeToServiceListMap: Map<string, IAurumService[]>;
  txnHitoryList: ITransactionHistory[];
  paidAmount: number;
  discountId: number;
  distinctServiceType: string[];
  invoiceTitle: string;
  printDate: Date = new Date();
  rateTypePriceMap: Map<string, number> = new Map();

  showXrayNote: boolean;
  showMeltingNote: boolean;
  showCalculatedMeltingNote: boolean;

  shopHeader = SHOP_HEADER;
  generatingPdf = false;

  amountInWordsStr: string;

  totalQuantity: number;
  totalWeight: number;
  totalAlloyWeight: number;

  hallmarkImageUrl: string | null = null;

  constructor(
    private route: ActivatedRoute,
    protected router: Router,
    protected customerService: CustomerService,
    protected aurumServiceService: AurumServiceService,
    protected voucherService: VoucherService,
    protected transactionHistoryService: TransactionHistoryService,
    private amountInWordsService: AmountInWords,
    protected rateService: RateService,
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

      if (this.voucher.hallmarkImage) {
        this.hallmarkImageUrl = `data:${this.voucher.hallmarkImageContentType ?? 'image/png'};base64,${this.voucher.hallmarkImage}`;
      }

      this.amountInWordsStr = this.amountInWordsService.convertNumberToWords(this.voucher.totalPayableAmount);
    });
  }

  fetchCustomer(id: number) {
    this.customerService.find(id).subscribe(data => {
      this.customer = data.body;
    });
  }

  fetchAurumServices(voucherId: number) {
    this.totalQuantity = 0;
    this.totalWeight = 0;
    this.totalAlloyWeight = 0;

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

        this.totalQuantity += service.quantity;
        this.totalWeight += service.weight;
        this.totalAlloyWeight += service.alloyQuantity;
      });
    });
  }

  fetchTxnHistory(voucherNo: string) {
    this.paidAmount = 0;
    this.transactionHistoryService.query({ 'voucherNo.equals': voucherNo }).subscribe(data => {
      this.txnHitoryList = data.body;
      this.txnHitoryList.map(txn => {
        if (txn.tag === 'RECEIVE') this.paidAmount += txn.amount;
        if (txn.tag === 'DISCOUNT') this.discountId = txn.id;
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

  /** Download the narrow 78mm POS receipt as a single continuous-page PDF. */
  downloadPosPdf() {
    this.elementToPosPdf('posReceipt', 78, `POS-${this.voucherNumber}.pdf`);
  }

  /** Download the full detailed report as an A4 (multi-page) PDF. */
  downloadReportPdf() {
    this.elementToA4Pdf('reportSection', `Report-${this.voucherNumber}.pdf`);
  }

  /** Snapshot an element to a single continuous PDF page of the given width (mm). */
  private elementToPosPdf(elementId: string, widthMm: number, fileName: string) {
    const el = document.getElementById(elementId);
    if (!el) return;
    this.generatingPdf = true;
    // JPEG@0.85 + stream compression at scale 2 keeps the receipt crisp while
    // shrinking the file dramatically (a lossless PNG here ran to several MB).
    html2canvas(el, { scale: 2, backgroundColor: '#ffffff' })
      .then(canvas => {
        const imgHeightMm = (canvas.height * widthMm) / canvas.width;
        const pdf = new jsPDF({ unit: 'mm', format: [widthMm, imgHeightMm], compress: true });
        pdf.addImage(canvas.toDataURL('image/jpeg', 0.85), 'JPEG', 0, 0, widthMm, imgHeightMm, undefined, 'FAST');
        pdf.save(fileName);
      })
      .finally(() => (this.generatingPdf = false));
  }

  /** Snapshot an element and paginate it across A4 pages. */
  private elementToA4Pdf(elementId: string, fileName: string) {
    const el = document.getElementById(elementId);
    if (!el) return;
    this.generatingPdf = true;
    // Keep the file small: moderate raster scale, then embed each A4 page as its
    // OWN compressed JPEG slice. (Re-adding the whole tall PNG per page produced
    // multi-MB files; slicing + JPEG keeps a typical report well under ~300 KB.)
    html2canvas(el, { scale: 1.5, backgroundColor: '#ffffff' })
      .then(canvas => {
        const pdf = new jsPDF({ unit: 'mm', format: 'a4', compress: true });
        const pageWidthMm = pdf.internal.pageSize.getWidth();
        const pageHeightMm = pdf.internal.pageSize.getHeight();
        // Pixel height of one A4 page at the canvas' resolution.
        const pageHeightPx = Math.floor((canvas.width * pageHeightMm) / pageWidthMm);

        const sliceCanvas = document.createElement('canvas');
        const ctx = sliceCanvas.getContext('2d');
        sliceCanvas.width = canvas.width;

        let renderedPx = 0;
        let firstPage = true;
        while (renderedPx < canvas.height) {
          const slicePx = Math.min(pageHeightPx, canvas.height - renderedPx);
          sliceCanvas.height = slicePx;
          ctx.fillStyle = '#ffffff';
          ctx.fillRect(0, 0, sliceCanvas.width, slicePx);
          ctx.drawImage(canvas, 0, renderedPx, canvas.width, slicePx, 0, 0, canvas.width, slicePx);

          if (!firstPage) pdf.addPage();
          firstPage = false;
          const sliceHeightMm = (slicePx * pageWidthMm) / canvas.width;
          pdf.addImage(sliceCanvas.toDataURL('image/jpeg', 0.8), 'JPEG', 0, 0, pageWidthMm, sliceHeightMm, undefined, 'FAST');
          renderedPx += slicePx;
        }
        pdf.save(fileName);
      })
      .finally(() => (this.generatingPdf = false));
  }

  adjustDiscount() {
    this.router.navigate(['transaction-history/' + this.discountId + '/edit']);
  }

  goBackToViewer() {
    this.router.navigate(['/voucher-viewer'], { queryParams: { voucherNo: this.voucherNumber } });
  }

  onHallmarkImageSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) {
      return;
    }
    const file = input.files[0];
    const reader = new FileReader();
    reader.onload = () => {
      const result = reader.result as string; // data:<mime>;base64,<data>
      this.hallmarkImageUrl = result;
      const base64 = result.substring(result.indexOf(',') + 1);
      this.voucherService
        .partialUpdate({
          id: this.voucher.id,
          hallmarkImage: base64,
          hallmarkImageContentType: file.type,
        })
        .subscribe();
    };
    reader.readAsDataURL(file);
  }

  ngOnDestroy() {}
}
