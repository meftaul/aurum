import { Component, OnInit, OnDestroy, ViewChild, ElementRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { ICustomer } from 'app/entities/customer/customer.model';
import { TransactionService } from '../services/service-api/transaction.service';
import { VOUCHER_STATUS, SERVICE_LIST_COLUMNS, AURUM_SERVICE_LIST, ALLOY_TYPE } from '../services/domain/transaction.models';
import { Subscription } from 'rxjs';
import { EventManager } from 'app/core/util/event-manager.service';
import { AlertService } from 'app/core/util/alert.service';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { IVoucher } from 'app/entities/voucher/voucher.model';
import { IAurumService } from 'app/entities/aurum-service/aurum-service.model';
import moment from 'moment';
import { VoucherStatus } from 'app/entities/enumerations/voucher-status.model';
import { KaratService } from 'app/entities/karat/service/karat.service';
import { RateService } from 'app/entities/rate/service/rate.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { ItemService } from 'app/entities/item/service/item.service';
import { CustomVoucherDto } from 'app/entities/custom.voucher.model';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';

const EXTRA_CHARGE_FOR_PER_GRAM = 1;
const VORI_TO_GRAM = 11.6638125; // = 1 vori
const VAT_RATE = 0.15;
const MELTING_TYPES = ['Normal Melting', 'Calculated Melting'];

@Component({
  standalone: false,
  selector: 'jhi-aurum-transaction',
  templateUrl: './transaction.component.html',
  styleUrls: ['./transaction.component.scss'],
})
export class TransactionComponent implements OnInit, OnDestroy {
  // First focusable field of the entry form — used to bounce focus back after a line is added.
  @ViewChild('firstField') firstField: ElementRef<HTMLElement>;

  account: Account;
  customerID: number;
  cusromerSearchingValue: string;
  searchCategory: string;
  customer: ICustomer;
  voucherForm: FormGroup;
  aurumServiceForm: FormGroup;

  selectedService = 'X-Ray';
  printDate = new Date();
  savedVoucherNumber: string;

  showBtnForCalculatedMelting = false;
  vatChecked = false;
  reportChargeChecked = false;
  isReportChargeDisabled = true;
  reportCharge = 50;

  aurumServiceList: IAurumService[] = [];
  calculateTotalAmount = 0;
  totalAmount = 0;
  payableTotalAmount = 0;
  amountDue = 0;
  selectedServiceCharge = 0;
  karatParcentDifference = 0;

  eventSubscriber: Subscription;
  loading = false;

  searchCategories: any[] = [
    { value: 'id', viewValue: 'Customer ID' },
    { value: 'phone', viewValue: 'Phone Number' },
  ];

  aurumServiceDropdownData: any[] = AURUM_SERVICE_LIST;
  voucherStatus: any[] = VOUCHER_STATUS;
  alloyTypeList: any[] = ALLOY_TYPE;
  karatList: any[] = [];
  itemDropdownList: any[] = [];
  rateTypePriceMap: Map<string, number> = new Map();
  karatTypePercentMap: Map<string, number> = new Map();
  serviceListColumns: string[] = SERVICE_LIST_COLUMNS;

  customerForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    protected jhiAlertService: AlertService,
    private transactionService: TransactionService,
    protected customerService: CustomerService,
    protected karatService: KaratService,
    protected rateService: RateService,
    protected itemService: ItemService,
    private accountService: AccountService,
    protected eventManager: EventManager,
    private modalService: NgbModal,
  ) {}

  ngOnInit() {
    this.prepareAurumServiceForm();
    this.prepareVoucherForm();
    this.prepareCustomerForm({} as ICustomer);
    this.searchCategory = 'phone';

    this.fetchKaratList();
    this.fetchRateList();
    this.fetchItemList();

    this.accountService.identity().subscribe((account: Account) => {
      this.account = account;
    });
  }

  // ******************** CUSTOMER ******************** START
  searchCustomer() {
    if (!this.cusromerSearchingValue) {
      return;
    }
    const criteria =
      this.searchCategory === 'id' ? { 'customId.equals': this.cusromerSearchingValue } : { 'phone.equals': this.cusromerSearchingValue };
    this.customerService.query(criteria).subscribe(
      data => {
        if (data.body && data.body.length === 0) {
          this.jhiAlertService.addAlert({ type: 'warning', message: 'Customer not found.' });
        }
        this.customer = data.body[0];
        this.customerID = this.customer ? this.customer.id : 0;
      },
      () => {
        this.customer = null;
        this.jhiAlertService.addAlert({ type: 'warning', message: 'Customer not found.' });
      },
    );
  }

  // Clear the selected customer so staff can look up a different one without leaving the page.
  changeCustomer() {
    this.customer = null;
    this.customerID = 0;
    this.cusromerSearchingValue = null;
  }

  prepareCustomerForm(customerData: ICustomer) {
    this.customerForm = this.formBuilder.group({
      firstName: [customerData.firstName, [Validators.required]],
      lastName: [customerData.lastName],
      phone: [customerData.phone, [Validators.required, Validators.minLength(11), Validators.maxLength(11)]],
      email: [customerData.email, [Validators.email]],
      address: [customerData.address],
      reference: [customerData.reference],
      customId: [customerData.customId, [Validators.required]],
    });
  }

  showCreateCustomerDialog(createCustomerDialog) {
    this.modalService.open(createCustomerDialog, { centered: true, size: 'lg' });
  }

  closeCreateCustomerDialog(cusModal) {
    cusModal.close();
    this.customerForm.reset();
  }

  createCustomer(cusModal) {
    this.markFormGroupAsTouched(this.customerForm);
    if (this.customerForm.invalid) {
      return;
    }

    const customer: any = this.customerForm.value;
    this.customerService.create(customer).subscribe(
      response => {
        if (response.body) {
          this.customer = response.body;
          this.customerID = this.customer ? this.customer.id : 0;
          cusModal.close();
          this.customerForm.reset();
        } else {
          this.customer = null;
          this.jhiAlertService.addAlert({ type: 'warning', message: 'Customer not found.' });
        }
      },
      () => {
        this.customer = null;
      },
    );
  }
  // ****************************** CUSTOMER ****************************** END

  // ****************************** AURUM SERVICE ****************************** START
  prepareAurumServiceForm() {
    this.aurumServiceForm = this.formBuilder.group({
      serviceType: ['', [Validators.required]],
      itemName: [''],
      karatType: [''],
      expectedKaratType: [''],
      addedAlloy: [''],
      alloyQuantity: ['', [Validators.maxLength(11), Validators.max(999999999.99)]],
      rate: ['', [Validators.required, Validators.maxLength(11), Validators.max(999999999.99)]],
      quantity: ['', [Validators.maxLength(3), Validators.max(9999)]],
      weight: ['', [Validators.required, Validators.maxLength(11), Validators.max(999999999.99)]],
      freeCheck: ['', [Validators.min(0), Validators.max(1)]],
      hallMarkedText: ['', Validators.maxLength(50)],
      weightOfFreeCheck: ['', [Validators.maxLength(11), Validators.max(999999999.99)]],

      // only for weight calculation
      weightVori: [''],
      weightAna: [''],
      weightRotti: [''],
      weightPoint: [''],
    });
  }

  addService() {
    this.markFormGroupAsTouched(this.aurumServiceForm);
    if (this.aurumServiceForm.invalid) {
      this.jhiAlertService.addAlert({ type: 'warning', message: 'Form Invalid.' });
      return;
    }

    const form = this.aurumServiceForm.controls;
    if (!form.quantity.value) {
      form.quantity.setValue(1);
    }

    const aurumServiceTemp = {} as IAurumService;
    aurumServiceTemp.serviceType = form.serviceType.value;
    aurumServiceTemp.itemName = form.itemName.value;
    aurumServiceTemp.karatType = form.karatType.value;
    aurumServiceTemp.rate = form.rate.value;
    aurumServiceTemp.serviceCharge = 0;
    aurumServiceTemp.quantity = form.quantity.value;
    aurumServiceTemp.amount = +form.rate.value * +form.quantity.value;
    aurumServiceTemp.weight = form.weight.value;

    aurumServiceTemp.freeCheck = form.freeCheck.value;
    aurumServiceTemp.hallMarkedText = form.hallMarkedText.value;
    aurumServiceTemp.weightOfFreeCheck = form.weightOfFreeCheck.value;

    if (form.serviceType.value === 'Calculated Melting') {
      aurumServiceTemp.expectedKaratType = form.expectedKaratType.value;
      aurumServiceTemp.addedAlloy = form.addedAlloy.value;
      aurumServiceTemp.alloyQuantity = form.alloyQuantity.value;
    }

    this.aurumServiceList = [aurumServiceTemp, ...this.aurumServiceList];
    this.recompute();
    this.resetServiceForm();
    this.focusFirstField();
  }

  deleteService(index) {
    this.aurumServiceList.splice(index, 1);
    this.aurumServiceList = [...this.aurumServiceList];
    this.recompute();
  }

  // Segmented service-type buttons: write the control then run the same logic the
  // old <mat-select> (ngModelChange) used to. Ignored once a line is added (locked).
  selectService(value: string) {
    if (this.aurumServiceList.length > 0) {
      return;
    }
    this.aurumServiceForm.controls.serviceType.setValue(value);
    this.serviceTypeChange(value);
  }

  serviceTypeChange(event) {
    this.selectedService = event;
    const form = this.aurumServiceForm.controls;

    if (this.selectedService === 'X-Ray') {
      form.itemName.setValidators([Validators.required]);
      form.karatType.clearValidators();
      form.expectedKaratType.clearValidators();
      form.addedAlloy.clearValidators();
      form.alloyQuantity.clearValidators();
      form.quantity.setValidators([Validators.required]);
      form.freeCheck.setValidators([Validators.required, Validators.min(0), Validators.max(1)]);
      form.hallMarkedText.clearValidators();
      this.updateFormValueAndValidity();
      form.quantity.setValue(1);
      form.freeCheck.setValue(0); // sensible default so Enter-to-add passes validation
      this.isReportChargeDisabled = true;
      this.reportChargeChecked = false;
    } else if (this.selectedService === 'Hallmark') {
      form.itemName.setValidators([Validators.required]);
      form.karatType.setValidators([Validators.required]);
      form.expectedKaratType.clearValidators();
      form.addedAlloy.clearValidators();
      form.alloyQuantity.clearValidators();
      form.quantity.setValidators([Validators.required]);
      form.freeCheck.clearValidators();
      this.updateFormValueAndValidity();
      form.quantity.setValue(1);
      this.isReportChargeDisabled = true;
      this.reportChargeChecked = false;
    } else if (this.selectedService === 'Normal Melting') {
      form.itemName.clearValidators();
      form.karatType.clearValidators();
      form.expectedKaratType.clearValidators();
      form.addedAlloy.clearValidators();
      form.alloyQuantity.clearValidators();
      form.quantity.clearValidators();
      form.freeCheck.clearValidators();
      form.hallMarkedText.clearValidators();
      this.updateFormValueAndValidity();
      form.quantity.setValue(1);
      this.isReportChargeDisabled = false;
    } else if (this.selectedService === 'Calculated Melting') {
      form.itemName.clearValidators();
      form.karatType.setValidators([Validators.required]);
      form.expectedKaratType.setValidators([Validators.required]);
      form.addedAlloy.setValidators([Validators.required]);
      form.alloyQuantity.setValidators([Validators.required]);
      form.quantity.clearValidators();
      form.freeCheck.clearValidators();
      form.hallMarkedText.clearValidators();
      this.updateFormValueAndValidity();
      form.quantity.setValue(1);
      this.isReportChargeDisabled = false;
    }

    const weight = +form.weight.value || 0;
    this.selectedServiceCharge = this.computeRate(event, weight);
    form.rate.setValue(this.selectedServiceCharge);

    this.showBtnForCalculatedMelting = event === 'Calculated Melting';
    this.recompute();
    this.focusFirstField();
  }

  updateFormValueAndValidity() {
    const form = this.aurumServiceForm.controls;
    const fields = [
      'itemName',
      'karatType',
      'expectedKaratType',
      'addedAlloy',
      'alloyQuantity',
      'weight',
      'quantity',
      'freeCheck',
      'hallMarkedText',
      'weightOfFreeCheck',
    ];
    fields.forEach(name => {
      form[name].setValue(null);
      form[name].updateValueAndValidity();
    });
  }

  resetServiceForm() {
    this.selectedServiceCharge = 0;
    this.karatParcentDifference = 0;
    this.aurumServiceForm.reset();
    this.aurumServiceForm.controls.serviceType.setValue(this.aurumServiceList[0].serviceType);
    // re-apply the per-type defaults for the next line of the same service type
    if (this.selectedService === 'X-Ray') {
      this.aurumServiceForm.controls.quantity.setValue(1);
      this.aurumServiceForm.controls.freeCheck.setValue(0);
    } else if (this.selectedService === 'Hallmark') {
      this.aurumServiceForm.controls.quantity.setValue(1);
    } else {
      this.aurumServiceForm.controls.quantity.setValue(1);
    }
  }
  // ****************************** AURUM SERVICE ****************************** END

  // ****************************** CALCULATE BILL ****************************** START
  // Single source of truth for every derived figure. Always recomputes from the
  // line items + the report-charge / VAT / discount / paid inputs — never by
  // incrementally mutating a running total (which used to drift).
  private recompute(): void {
    if (this.aurumServiceList.length === 0) {
      this.calculateTotalAmount = 0;
      this.totalAmount = 0;
      this.payableTotalAmount = 0;
      this.amountDue = 0;
      this.voucherForm.controls.vat.setValue(0);
      return;
    }

    const lineItemsTotal = this.aurumServiceList.reduce((sum, s) => sum + (+s.amount || 0), 0);
    const reportChargeAmount = this.reportChargeChecked && !this.isReportChargeDisabled ? +this.reportCharge || 0 : 0;
    const calcBase = this.round2(lineItemsTotal + reportChargeAmount);
    this.calculateTotalAmount = calcBase;

    const vatAmount = this.vatChecked ? this.round2(calcBase * VAT_RATE) : 0;
    this.voucherForm.controls.vat.setValue(vatAmount);
    this.totalAmount = this.round2(calcBase + vatAmount);

    const discount = +this.voucherForm.controls.disountAmount.value || 0;
    this.payableTotalAmount = this.round2(this.totalAmount - discount);

    const paid = +this.voucherForm.controls.paidAmount.value || 0;
    this.amountDue = this.round2(this.payableTotalAmount - paid);
  }

  private round2(n: number): number {
    return +(Number.isFinite(n) ? n : 0).toFixed(2);
  }

  // All bill inputs funnel through recompute() — no per-keystroke valueChanges
  // subscriptions (those used to stack up and leak).
  onVatCheckboxValueChange(event) {
    this.vatChecked = event.checked;
    this.recompute();
  }

  onReportChargeCheckboxValueChange(event) {
    this.reportChargeChecked = event.checked;
    this.recompute();
  }

  onReportChargeValueChange(event) {
    this.recompute();
  }

  onVatValueChange(event) {
    this.recompute();
  }

  onDiscountValueChange(event) {
    this.recompute();
  }

  onTotalPayableValueChange(event) {
    this.recompute();
  }
  // ****************************** CALCULATE BILL ****************************** END

  // ****************************** VOUCHER ****************************** START
  prepareVoucherForm() {
    let d = new Date().toISOString();
    d = d.substring(0, d.length - 5);

    this.voucherForm = this.formBuilder.group({
      voucherNo: [''],
      boxNumber: ['', [Validators.required, Validators.maxLength(50)]],
      deliveryDate: [d, [Validators.required]],
      calculatedTotalAmount: [0],
      disountAmount: [0, [Validators.required, Validators.maxLength(11)]],
      vat: [0, [Validators.required, Validators.maxLength(11)]],
      paidAmount: ['', [Validators.maxLength(11), Validators.required]],
    });
  }

  // Pre-flight guards, shared by the confirm dialog and the direct "Take Payment" path.
  private validateBeforePayment(): boolean {
    this.markFormGroupAsTouched(this.voucherForm);
    if (!this.customerID || this.customerID === 0) {
      this.jhiAlertService.addAlert({ type: 'warning', message: 'Customer not found.' });
      return false;
    }
    if (this.aurumServiceList.length === 0) {
      this.jhiAlertService.addAlert({ type: 'warning', message: 'Add atleast one service.' });
      return false;
    }
    if (this.voucherForm.invalid) {
      this.jhiAlertService.addAlert({ type: 'warning', message: 'Invalid Data.' });
      return false;
    }
    return true;
  }

  confirmMakePayment(confirmDialog) {
    if (!this.validateBeforePayment()) {
      return;
    }
    this.modalService.open(confirmDialog, { centered: true });
  }

  makePayment() {
    if (!this.validateBeforePayment()) {
      return;
    }
    if (this.loading === true) {
      alert('Your have one pending request. Please wait few moments.');
      return;
    }

    const paidAmount = +this.voucherForm.controls.paidAmount.value;
    if (paidAmount > this.payableTotalAmount) {
      this.jhiAlertService.addAlert({ type: 'danger', message: 'Paid amount can not be greater than payable amount' });
      return;
    }

    this.loading = true;

    // Build the line items fresh so a retry never double-stamps the report charge.
    const aurumServices: IAurumService[] = this.aurumServiceList.map(s => ({ ...s }));
    if (this.reportChargeChecked && !this.isReportChargeDisabled) {
      aurumServices[0].serviceCharge = +this.reportCharge || 0;
    }

    const voucherTemp: any = {};
    voucherTemp.voucherNo = moment().toString();
    voucherTemp.customerId = this.customerID;
    voucherTemp.boxNumber = this.voucherForm.controls.boxNumber.value;
    voucherTemp.calculatedTotalAmount = this.calculateTotalAmount;
    voucherTemp.disountAmount = this.voucherForm.controls.disountAmount.value;
    voucherTemp.vat = this.voucherForm.controls.vat.value;
    voucherTemp.totalPayableAmount = this.payableTotalAmount;
    voucherTemp.aurumServices = aurumServices;
    voucherTemp.dateCreated = moment(new Date(), DATE_TIME_FORMAT);
    voucherTemp.deliveryDate =
      this.voucherForm.controls.deliveryDate.value !== null
        ? moment(this.voucherForm.controls.deliveryDate.value, DATE_TIME_FORMAT)
        : undefined;
    // Compare rounded figures so a fully-paid voucher is never saved as DUE on a float wobble.
    voucherTemp.status = this.round2(this.payableTotalAmount) === this.round2(paidAmount) ? VoucherStatus.PAID : VoucherStatus.DUE;
    voucherTemp.addedBy = this.account.login;
    voucherTemp.deliveryStatus = aurumServices[0].serviceType === 'X-Ray' || aurumServices[0].serviceType === 'Calculated Melting';

    const customVoucherDto = new CustomVoucherDto();
    customVoucherDto.voucher = voucherTemp;
    customVoucherDto.paidAmount = paidAmount;

    this.transactionService.create(customVoucherDto).subscribe(
      data => {
        // method return IVoucher not CustomVoucher
        this.savedVoucherNumber = data.body.voucherNo;
        this.resetVoucherForm();
        this.jhiAlertService.addAlert({
          type: 'success',
          message: 'Transaction completed with voucher number '.concat(this.savedVoucherNumber),
        });
        this.loading = false;
        this.router.navigate(['/invoice', this.savedVoucherNumber]);
      },
      () => {
        alert('Error in saving voucher. Please try again.');
        this.loading = false;
      },
    );
  }

  resetVoucherForm() {
    this.voucherForm.reset();
    this.aurumServiceList = [];
    this.selectedServiceCharge = 0;
    this.karatParcentDifference = 0;
    this.aurumServiceForm.reset();
    this.vatChecked = false;
    this.reportChargeChecked = false;
    this.calculateTotalAmount = 0;
    this.totalAmount = 0;
    this.payableTotalAmount = 0;
    this.amountDue = 0;
    this.customer = null;
    this.cusromerSearchingValue = null;
  }
  // ****************************** VOUCHER ****************************** END

  // GET KARAT LIST FROM SERVICE
  fetchKaratList() {
    this.karatService.query({ size: 100 }).subscribe(data => {
      if (data.body && data.body.length !== 0) {
        data.body.map(karat => {
          this.karatList = [...this.karatList, { value: karat.karatType, viewValue: karat.karatType }];
          this.karatTypePercentMap.set(karat.karatType, karat.purityPercent);
        });
      }
    });
  }

  // GET RATE LIST & MAP RATE_TYPE TO PRICE FROM SERVICE
  fetchRateList() {
    this.rateService.query().subscribe(data => {
      if (data.body && data.body.length !== 0) {
        data.body.map(rate => {
          this.rateTypePriceMap.set(rate.rateType, rate.unitPrice);
        });
      }
    });
  }

  // GET ITEM LIST TO SHOW IN DROPDOWN
  fetchItemList() {
    this.itemService.query({ size: 150 }).subscribe(data => {
      if (data.body && data.body.length !== 0) {
        data.body.map(item => {
          this.itemDropdownList = [...this.itemDropdownList, { value: item.name, viewValue: item.name }];
        });
      }
    });
  }

  // ****************************** WEIGHT & RATE ****************************** START
  // Single rate formula reused by both the type change and the weight change so they
  // can't disagree (the type-change path used to use a wrong `weight % 100`).
  private computeRate(type: string, weight: number): number {
    const base = this.rateTypePriceMap.get(type) ?? 0;
    if (weight > 100 && MELTING_TYPES.includes(type)) {
      return this.round2(base + (weight - 100) * EXTRA_CHARGE_FOR_PER_GRAM);
    }
    return base;
  }

  onWeightValueChange(event) {
    const form = this.aurumServiceForm.controls;
    const gramValue = form.weight.value ? +form.weight.value : 0;

    // weight readout: vori / ana / rotti / point
    const voriValue = +(gramValue / VORI_TO_GRAM);
    const anaValue = +(voriValue % 1).toFixed(7) * 16;
    const rottiValue = +(anaValue % 1).toFixed(7) * 6;
    const pointValue = +(rottiValue % 1).toFixed(7) * 10;
    form.weightVori.setValue(Math.floor(voriValue));
    form.weightAna.setValue(Math.floor(anaValue));
    form.weightRotti.setValue(Math.floor(rottiValue));
    form.weightPoint.setValue(Math.floor(pointValue));

    const serviceTypeTemp = form.serviceType.value;
    if (serviceTypeTemp) {
      this.selectedServiceCharge = this.computeRate(serviceTypeTemp, gramValue);
      form.rate.setValue(this.selectedServiceCharge);
    }

    // weight feeds the alloy quantity for Calculated Melting — recompute it without
    // wiping the karat selections the user already made.
    this.recomputeAlloy();
  }

  onKaratTypeChange(event) {
    this.recomputeAlloy();
  }

  onExpectedKaratTypeChange(event) {
    this.recomputeAlloy();
  }

  onAddedAlloyChange(event) {
    this.applyAlloyPrice();
  }

  // Derives alloy quantity from the karat-purity difference and validates that the
  // source karat is higher than the expected karat. Shared by weight/karat handlers.
  private recomputeAlloy(): void {
    const form = this.aurumServiceForm.controls;
    if (form.serviceType.value !== 'Calculated Melting') {
      return;
    }
    const karat = form.karatType.value;
    const expected = form.expectedKaratType.value;
    if (!karat || !expected) {
      return;
    }

    const karatPct = this.karatTypePercentMap.get(karat) ?? 0;
    const expectedPct = this.karatTypePercentMap.get(expected) ?? 0;
    this.karatParcentDifference = karatPct - expectedPct;

    const gramValue = form.weight.value ? +form.weight.value : 0;
    const addedAlloyWeight = +((gramValue * this.karatParcentDifference) / 100).toFixed(2);
    form.alloyQuantity.setValue(addedAlloyWeight);

    if (this.karatParcentDifference <= 0) {
      form.karatType.setErrors({ percentDiffError: true });
      form.expectedKaratType.setErrors({ percentDiffError: true });
    } else {
      form.karatType.setErrors(null);
      form.expectedKaratType.setErrors(null);
      this.applyAlloyPrice();
    }
  }

  // Rate = base service charge + (alloy unit price × alloy quantity). If no alloy rate
  // exists in the rate master the alloy contributes 0 (no NaN), leaving the base rate.
  private applyAlloyPrice(): void {
    const form = this.aurumServiceForm.controls;
    const alloy = form.addedAlloy.value;
    if (!alloy) {
      form.rate.setValue(this.selectedServiceCharge);
      return;
    }
    const alloyRate = this.rateTypePriceMap.get(alloy) ?? 0;
    const qty = +form.alloyQuantity.value || 0;
    form.rate.setValue(this.round2(this.selectedServiceCharge + alloyRate * qty));
  }
  // ****************************** WEIGHT & RATE ****************************** END

  ngOnDestroy() {
    // no long-lived subscriptions to tear down
  }

  // Util methods
  numberOnly(event): boolean {
    const charCode = event.which ? event.which : event.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
      return false;
    }
    return true;
  }

  markFormGroupAsTouched(formGroup: FormGroup) {
    Object.values(formGroup.controls).forEach(control => {
      control.markAsDirty();
      control.markAllAsTouched();
    });
  }

  private focusFirstField(): void {
    setTimeout(() => this.firstField?.nativeElement?.focus(), 0);
  }

  printPage() {
    window.print();
  }
}
