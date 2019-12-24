import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { ICustomer, Customer } from 'app/shared/model/customer.model';
import { TransactionService } from '../services/service-api/transaction.service';
import { VOUCHER_STATUS, SERVICE_LIST_COLUMNS, AURUM_SERVICE_LIST, ALLOY_TYPE } from '../services/domain/transaction.models';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { CustomerService } from 'app/entities/customer/customer.service';
import { Voucher } from 'app/shared/model/voucher.model';
import { AurumService } from 'app/shared/model/aurum-service.model';
import * as moment from 'moment';
import { VoucherStatus } from 'app/shared/model/enumerations/voucher-status.model';
import { KaratService } from 'app/entities/karat/karat.service';
import { RateService } from 'app/entities/rate/rate.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
import { ItemService } from 'app/entities/item/item.service';
import { CustomVoucherDto } from 'app/shared/model/custom.voucher.model';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';

const VORI_TO_GRAM = 11.6638125; // = 1 vori
// const ANA_TO_GRAM = 0.72898828125;
// const ROTTI_TO_GRAM = 0.121498046875;
// const POINT_TO_GRAM = 0.0121498046875;

@Component({
  selector: 'jhi-aurum-transaction',
  templateUrl: './transaction.component.html',
  styleUrls: ['./transaction.component.scss']
})
export class TransactionComponent implements OnInit, OnDestroy {
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

  aurumServiceList: AurumService[] = [];
  calculateTotalAmount = 0;
  totalAmount = 0;
  payableTotalAmount = 0;
  amountDue = 0;
  selectedServiceCharge = 0;
  karatParcentDifference = 0;

  eventSubscriber: Subscription;

  searchCategories: any[] = [{ value: 'phone', viewValue: 'Phone Number' }, { value: 'id', viewValue: 'Customer ID' }];

  itemNames: any[] = [
    { value: 'Gold X-Ray', viewValue: 'Gold X-Ray' },
    { value: '1 Gold Check (Mark)', viewValue: '1 Gold Check (Mark)' },
    { value: 'Anklet', viewValue: 'Anklet' },
    { value: 'Ayesti', viewValue: 'Ayesti' },
    { value: 'Baju', viewValue: 'Baju' }
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
    protected jhiAlertService: JhiAlertService,
    private transactionService: TransactionService,
    protected customerService: CustomerService,
    protected karatService: KaratService,
    protected rateService: RateService,
    protected itemService: ItemService,
    private accountService: AccountService,
    protected eventManager: JhiEventManager,
    private modalService: NgbModal
  ) {}

  ngOnInit() {
    this.prepareAurumServiceForm();
    this.prepareVoucherForm();
    this.prepareCustomerForm(new Customer());
    this.searchCategory = 'phone';

    this.fetchKaratList();
    this.fetchRateList();
    this.fetchItemList();

    this.accountService.identity().then((account: Account) => {
      this.account = account;
    });
  }

  // ******************** CUSTOMER ******************** START
  searchCustomer() {
    if (this.searchCategory === 'id') {
      this.customerService.find(+this.cusromerSearchingValue).subscribe(
        data => {
          if (!data.body) this.jhiAlertService.warning('Customer not found.');
          this.customer = data.body;
          this.customerID = this.customer ? this.customer.id : 0;
        },
        error => {
          this.customer = null;
          this.jhiAlertService.warning('Customer not found.');
        }
      );
    } else {
      this.customerService.query({ 'phone.equals': this.cusromerSearchingValue }).subscribe(
        data => {
          if (data.body && data.body.length === 0) this.jhiAlertService.warning('Customer not found.');
          this.customer = data.body[0];
          this.customerID = this.customer ? this.customer.id : 0;
        },
        error => {
          this.customer = null;
          this.jhiAlertService.warning('Customer not found.');
        }
      );
    }
  }

  prepareCustomerForm(customerData: Customer) {
    this.customerForm = this.formBuilder.group({
      firstName: [customerData.firstName, [Validators.required]],
      lastName: [customerData.lastName],
      phone: [customerData.phone, [Validators.required, Validators.minLength(11), Validators.maxLength(11)]],
      email: [customerData.email, [Validators.email]],
      address: [customerData.address],
      reference: [customerData.reference]
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

    let customer = new Customer();
    customer = this.customerForm.value;
    this.customerService.create(customer).subscribe(
      response => {
        if (response.body) {
          this.customer = response.body;
          this.customerID = this.customer ? this.customer.id : 0;
          cusModal.close();
          this.customerForm.reset();
        } else {
          this.customer = null;
          this.jhiAlertService.warning('Customer not found.');
        }
      },
      error => {
        this.customer = null;
      }
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
      alloyQuantity: [''],
      rate: ['', [Validators.required]],
      quantity: [''],
      weight: [''],
      freeCheck: ['', [Validators.min(0), Validators.max(1)]],
      hallMarkedText: [''],

      // only for weight calculation
      weightVori: [''],
      weightAna: [''],
      weightRotti: [''],
      weightPoint: ['']
    });
  }

  addService() {
    this.markFormGroupAsTouched(this.aurumServiceForm);
    if (this.aurumServiceForm.invalid) {
      this.jhiAlertService.warning('Form Invalid.');
      return;
    }

    if (
      this.aurumServiceForm.controls.quantity.value === null ||
      this.aurumServiceForm.controls.quantity.value === 0 ||
      this.aurumServiceForm.controls.quantity.value === ''
    ) {
      this.aurumServiceForm.controls.quantity.setValue(1);
    }

    const aurumServiceTemp = new AurumService();
    aurumServiceTemp.serviceType = this.aurumServiceForm.controls.serviceType.value;
    aurumServiceTemp.itemName = this.aurumServiceForm.controls.itemName.value;
    aurumServiceTemp.karatType = this.aurumServiceForm.controls.karatType.value;
    aurumServiceTemp.rate = this.aurumServiceForm.controls.rate.value;
    aurumServiceTemp.serviceCharge = 0;
    aurumServiceTemp.quantity = this.aurumServiceForm.controls.quantity.value;
    aurumServiceTemp.amount = +this.aurumServiceForm.controls.rate.value * +this.aurumServiceForm.controls.quantity.value;
    aurumServiceTemp.weight = this.aurumServiceForm.controls.weight.value;

    aurumServiceTemp.freeCheck = this.aurumServiceForm.controls.freeCheck.value;
    aurumServiceTemp.hallMarkedText = this.aurumServiceForm.controls.hallMarkedText.value;

    if (this.aurumServiceForm.controls.serviceType.value === 'Calculated Melting') {
      aurumServiceTemp.expectedKaratType = this.aurumServiceForm.controls.expectedKaratType.value;
      aurumServiceTemp.addedAlloy = this.aurumServiceForm.controls.addedAlloy.value;
      aurumServiceTemp.alloyQuantity = this.aurumServiceForm.controls.alloyQuantity.value;
    }

    this.aurumServiceList = [aurumServiceTemp, ...this.aurumServiceList];
    this.calculateTotalServiceCharge(this.aurumServiceList);
    this.resetServiceForm();
  }

  editService() {}

  deleteService(index) {
    this.aurumServiceList.splice(index, 1);
    this.aurumServiceList = [...this.aurumServiceList];
    this.calculateTotalServiceCharge(this.aurumServiceList);
  }

  calculateTotalServiceCharge(serviceList: AurumService[]) {
    this.calculateTotalAmount = 0;
    this.payableTotalAmount = 0;
    this.amountDue = 0;
    if (serviceList.length !== 0) {
      serviceList.map(service => {
        this.calculateTotalAmount += service.amount;
        this.totalAmount += service.amount;
        this.payableTotalAmount += service.amount;
        this.amountDue += service.amount;
      });
    }

    if (this.reportChargeChecked) {
      this.calculateTotalAmount += +this.reportCharge;
      const vatTemp = this.vatChecked ? +((this.calculateTotalAmount * 15) / 100).toFixed(2) : 0;
      this.totalAmount = +(this.calculateTotalAmount + vatTemp).toFixed(2);
      const discount = this.voucherForm.controls.disountAmount.value ? +this.voucherForm.controls.disountAmount.value : 0;
      this.payableTotalAmount = +(this.totalAmount - discount).toFixed(2);
      this.amountDue = +(this.totalAmount - discount).toFixed(2);
      this.voucherForm.controls.vat.setValue(vatTemp);
    }

    if (this.vatChecked) {
      // this.calculateTotalAmount += this.reportChargeChecked ? +this.reportCharge : 0;
      const vatTemp = +((this.calculateTotalAmount * 15) / 100).toFixed(2);
      this.totalAmount = +(this.calculateTotalAmount + vatTemp).toFixed(2);
      const discount = this.voucherForm.controls.disountAmount.value ? +this.voucherForm.controls.disountAmount.value : 0;
      this.payableTotalAmount = +(this.totalAmount - discount).toFixed(2);
      this.amountDue = +(this.totalAmount - discount).toFixed(2);
      this.voucherForm.controls.vat.setValue(vatTemp);
    }
  }

  serviceTypeChange(event) {
    this.selectedService = event;

    if (this.selectedService === 'X-Ray') {
      this.aurumServiceForm.controls.itemName.setValidators([Validators.required]);
      this.aurumServiceForm.controls.karatType.clearValidators();
      this.aurumServiceForm.controls.expectedKaratType.clearValidators();
      this.aurumServiceForm.controls.addedAlloy.clearValidators();
      this.aurumServiceForm.controls.alloyQuantity.clearValidators();
      this.aurumServiceForm.controls.weight.setValidators([Validators.required]);
      this.aurumServiceForm.controls.quantity.setValidators([Validators.required]);
      this.aurumServiceForm.controls.freeCheck.setValidators([Validators.required, Validators.min(0), Validators.max(1)]);
      this.aurumServiceForm.controls.hallMarkedText.clearValidators();
      this.aurumServiceForm.controls.quantity.setValue(1);
      this.updateFormValueAndValidity();
      this.isReportChargeDisabled = true;
    } else if (this.selectedService === 'Hallmark') {
      this.aurumServiceForm.controls.itemName.setValidators([Validators.required]);
      this.aurumServiceForm.controls.karatType.setValidators([Validators.required]);
      this.aurumServiceForm.controls.expectedKaratType.clearValidators();
      this.aurumServiceForm.controls.addedAlloy.clearValidators();
      this.aurumServiceForm.controls.alloyQuantity.clearValidators();
      this.aurumServiceForm.controls.weight.reset();
      this.aurumServiceForm.controls.weight.clearValidators();
      this.aurumServiceForm.controls.quantity.setValidators([Validators.required]);
      this.aurumServiceForm.controls.freeCheck.clearValidators();
      this.aurumServiceForm.controls.hallMarkedText.setValidators([Validators.required]);
      this.aurumServiceForm.controls.quantity.setValue(1);
      this.updateFormValueAndValidity();
      this.isReportChargeDisabled = true;
    } else if (this.selectedService === 'Normal Melting') {
      this.aurumServiceForm.controls.itemName.clearValidators();
      this.aurumServiceForm.controls.karatType.reset();
      this.aurumServiceForm.controls.karatType.clearValidators();
      this.aurumServiceForm.controls.expectedKaratType.clearValidators();
      this.aurumServiceForm.controls.addedAlloy.clearValidators();
      this.aurumServiceForm.controls.alloyQuantity.clearValidators();
      this.aurumServiceForm.controls.weight.setValidators([Validators.required]);
      this.aurumServiceForm.controls.quantity.clearValidators();
      this.aurumServiceForm.controls.freeCheck.clearValidators();
      this.aurumServiceForm.controls.hallMarkedText.clearValidators();
      this.aurumServiceForm.controls.quantity.setValue(1);
      this.updateFormValueAndValidity();
      this.isReportChargeDisabled = false;
    } else if (this.selectedService === 'Calculated Melting') {
      this.aurumServiceForm.controls.itemName.clearValidators();
      this.aurumServiceForm.controls.karatType.setValidators([Validators.required]);
      this.aurumServiceForm.controls.expectedKaratType.setValidators([Validators.required]);
      this.aurumServiceForm.controls.addedAlloy.setValidators([Validators.required]);
      this.aurumServiceForm.controls.alloyQuantity.setValidators([Validators.required]);
      this.aurumServiceForm.controls.weight.setValidators([Validators.required]);
      this.aurumServiceForm.controls.quantity.clearValidators();
      this.aurumServiceForm.controls.freeCheck.clearValidators();
      this.aurumServiceForm.controls.hallMarkedText.clearValidators();
      this.aurumServiceForm.controls.quantity.setValue(1);
      this.updateFormValueAndValidity();
      this.isReportChargeDisabled = false;
    }

    const weightTemp = this.aurumServiceForm.controls.weight.value ? +this.aurumServiceForm.controls.weight.value : 0;
    if (weightTemp <= 116) {
      this.selectedServiceCharge = this.rateTypePriceMap.get(event);
      this.aurumServiceForm.controls.rate.setValue(this.selectedServiceCharge);
    } else {
      const extraWeightToCharge = +(weightTemp % 116).toFixed(2);
      this.selectedServiceCharge = this.rateTypePriceMap.get(event) + extraWeightToCharge;
      this.aurumServiceForm.controls.rate.setValue(this.selectedServiceCharge);
    }
    if (event === 'Calculated Melting') {
      this.showBtnForCalculatedMelting = true;
    } else {
      this.showBtnForCalculatedMelting = false;
    }
  }

  updateFormValueAndValidity() {
    this.aurumServiceForm.controls.itemName.setValue(null);
    this.aurumServiceForm.controls.karatType.setValue(null);
    this.aurumServiceForm.controls.expectedKaratType.setValue(null);
    this.aurumServiceForm.controls.addedAlloy.setValue(null);
    this.aurumServiceForm.controls.alloyQuantity.setValue(null);
    this.aurumServiceForm.controls.weight.setValue(null);
    this.aurumServiceForm.controls.quantity.setValue(null);
    this.aurumServiceForm.controls.freeCheck.setValue(null);
    this.aurumServiceForm.controls.hallMarkedText.setValue(null);

    this.aurumServiceForm.controls.itemName.updateValueAndValidity();
    this.aurumServiceForm.controls.karatType.updateValueAndValidity();
    this.aurumServiceForm.controls.expectedKaratType.updateValueAndValidity();
    this.aurumServiceForm.controls.addedAlloy.updateValueAndValidity();
    this.aurumServiceForm.controls.alloyQuantity.updateValueAndValidity();
    this.aurumServiceForm.controls.weight.updateValueAndValidity();
    this.aurumServiceForm.controls.quantity.updateValueAndValidity();
    this.aurumServiceForm.controls.freeCheck.updateValueAndValidity();
    this.aurumServiceForm.controls.hallMarkedText.updateValueAndValidity();
  }

  resetServiceForm() {
    this.selectedServiceCharge = 0;
    this.karatParcentDifference = 0;
    this.aurumServiceForm.reset();
    this.aurumServiceForm.controls.serviceType.setValue(this.aurumServiceList[0].serviceType);
  }
  // ****************************** AURUM SERVICE ****************************** END

  // ****************************** CALCULATE BILL ****************************** START
  onVatValueChange(event) {
    this.voucherForm.controls.vat.valueChanges.subscribe(value => {
      if (value) {
        this.totalAmount = +(this.calculateTotalAmount + +value).toFixed(2);
        const discount = this.voucherForm.controls.disountAmount.value ? +this.voucherForm.controls.disountAmount.value : 0;
        this.payableTotalAmount = +(this.totalAmount - discount).toFixed(2);
        this.amountDue = +(this.totalAmount - discount).toFixed(2);
      } else {
        this.totalAmount = +(this.calculateTotalAmount + 0).toFixed(2);
        const discount = this.voucherForm.controls.disountAmount ? +this.voucherForm.controls.disountAmount : 0;
        this.payableTotalAmount = +(this.totalAmount - discount).toFixed(2);
        this.amountDue = +(this.totalAmount - discount).toFixed(2);
      }
    });
  }

  onReportChargeValueChange(event) {}

  onVatCheckboxValueChange(event) {
    if (event.checked) {
      const vatTemp = +((this.calculateTotalAmount * 15) / 100).toFixed(2);
      this.totalAmount = +(this.calculateTotalAmount + vatTemp).toFixed(2);
      const discount = this.voucherForm.controls.disountAmount.value ? +this.voucherForm.controls.disountAmount.value : 0;
      this.payableTotalAmount = +(this.totalAmount - discount).toFixed(2);
      this.amountDue = +(this.totalAmount - discount).toFixed(2);
      this.voucherForm.controls.vat.setValue(vatTemp);
    } else {
      this.totalAmount = +(this.calculateTotalAmount + 0).toFixed(2);
      const discount = this.voucherForm.controls.disountAmount.value ? +this.voucherForm.controls.disountAmount.value : 0;
      this.payableTotalAmount = +(this.totalAmount - discount).toFixed(2);
      this.amountDue = +(this.totalAmount - discount).toFixed(2);
      this.voucherForm.controls.vat.setValue(0);
    }
  }

  onReportChargeCheckboxValueChange(event) {
    if (event.checked) {
      this.calculateTotalAmount += +this.reportCharge;
    } else {
      this.calculateTotalAmount -= +this.reportCharge;
    }
    const vatTemp = this.vatChecked ? +((this.calculateTotalAmount * 15) / 100).toFixed(2) : 0;
    this.totalAmount = +(this.calculateTotalAmount + vatTemp).toFixed(2);
    const discount = this.voucherForm.controls.disountAmount.value ? +this.voucherForm.controls.disountAmount.value : 0;
    this.payableTotalAmount = +(this.totalAmount - discount).toFixed(2);
    this.amountDue = +(this.totalAmount - discount).toFixed(2);
    this.voucherForm.controls.vat.setValue(vatTemp);
  }

  onDiscountValueChange(event) {
    this.voucherForm.controls.disountAmount.valueChanges.subscribe(value => {
      if (value) {
        const discount = this.voucherForm.controls.disountAmount.value ? +this.voucherForm.controls.disountAmount.value : 0;
        this.payableTotalAmount = this.totalAmount - discount;
        this.amountDue = +(this.totalAmount - discount).toFixed(2);
      } else {
        this.payableTotalAmount = this.totalAmount - 0;
        this.amountDue = +(this.totalAmount - 0).toFixed(2);
      }
    });
  }

  onTotalPayableValueChange(event) {
    this.voucherForm.controls.paidAmount.valueChanges.subscribe(value => {
      if (value) {
        this.amountDue = +(this.payableTotalAmount - +value).toFixed(2);
      } else {
        this.amountDue = +(this.payableTotalAmount - 0).toFixed(2);
      }
    });
  }
  // ****************************** CALCULATE BILL ****************************** START

  // ****************************** VOUCHER ****************************** START
  prepareVoucherForm() {
    this.voucherForm = this.formBuilder.group({
      voucherNo: [''],
      boxNumber: ['', [Validators.required]],
      deliveryDate: ['', [Validators.required]],
      calculatedTotalAmount: [0],
      disountAmount: [0, [Validators.required]],
      vat: [0, [Validators.required]],
      paidAmount: [0]
    });
  }

  confirmMakePayment(confirmDialog) {
    this.markFormGroupAsTouched(this.voucherForm);
    if (!this.customerID || this.customerID === 0) {
      this.jhiAlertService.warning('Customer not found.');
      return;
    }
    if (this.aurumServiceList.length === 0) {
      this.jhiAlertService.warning('Add atleast one service.');
      return;
    }
    if (this.voucherForm.invalid) {
      this.jhiAlertService.warning('Invalid Data.');
      return;
    }
    this.modalService.open(confirmDialog, { centered: true });
  }

  makePayment() {
    // add report/service charge to aurumService's first item
    if (this.reportChargeChecked) {
      this.aurumServiceList[0].serviceCharge = +this.reportCharge;
    }

    const voucherTemp = new Voucher();
    voucherTemp.voucherNo = moment().toString();
    voucherTemp.customerId = this.customerID;
    voucherTemp.boxNumber = this.voucherForm.controls.boxNumber.value;
    voucherTemp.calculatedTotalAmount = this.calculateTotalAmount;
    voucherTemp.disountAmount = this.voucherForm.controls.disountAmount.value;
    voucherTemp.vat = this.voucherForm.controls.vat.value;
    voucherTemp.totalPayableAmount = this.payableTotalAmount;
    voucherTemp.aurumServices = this.aurumServiceList;
    voucherTemp.dateCreated = moment(new Date(), DATE_TIME_FORMAT);
    voucherTemp.deliveryDate =
      this.voucherForm.controls.deliveryDate.value !== null
        ? moment(this.voucherForm.controls.deliveryDate.value, DATE_TIME_FORMAT)
        : undefined;
    if (this.payableTotalAmount === +this.voucherForm.controls.paidAmount.value) voucherTemp.status = VoucherStatus.PAID;
    else voucherTemp.status = VoucherStatus.DUE;
    voucherTemp.addedBy = this.account.login;
    voucherTemp.deliveryStatus =
      this.aurumServiceList[0].serviceType === 'X-Ray' || this.aurumServiceList[0].serviceType === 'Calculated Melting';

    const customVoucherDto = new CustomVoucherDto();
    customVoucherDto.voucher = voucherTemp;
    customVoucherDto.paidAmount = +this.voucherForm.controls.paidAmount.value;

    // window.print();

    this.transactionService.create(customVoucherDto).subscribe(
      data => {
        // method return Voucher not CustomVoucher
        this.savedVoucherNumber = data.body.voucherNo;
        this.resetVoucherForm();
        this.jhiAlertService.success('Transaction completed with voucher number '.concat(this.savedVoucherNumber));

        this.router.navigate(['/invoice', this.savedVoucherNumber]);
      },
      error => {
        this.jhiAlertService.error('Error in saving voucher. ');
      }
    );
  }

  resetVoucherForm() {
    this.voucherForm.reset();
    this.resetServiceForm();
    this.aurumServiceList = [];
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
    this.karatService.query().subscribe(data => {
      /* eslint-disable no-console */
      console.log(data);
      /* eslint-enable no-console */
      if (data.body && data.body.length !== 0) {
        data.body.map(karat => {
          this.karatList = [{ value: karat.karatType, viewValue: karat.karatType + '(' + karat.purityPercent + '%)' }, ...this.karatList];
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
    this.itemService.query().subscribe(data => {
      if (data.body && data.body.length !== 0) {
        data.body.map(item => {
          this.itemDropdownList = [{ value: item.name, viewValue: item.name }, ...this.itemDropdownList];
        });
      }
    });
  }

  // VORI_TO_GRAM = 11.6638125;
  // ANA_TO_GRAM = 0.72898828125;
  // ROTTI_TO_GRAM = 0.121498046875;
  // POINT_TO_GRAM = 0.0121498046875;
  // ****************************** WEIGHT CALCULATION ****************************** START
  onWeightValueChange(event) {
    // reset some field value
    this.aurumServiceForm.controls.karatType.setValue(null);
    this.aurumServiceForm.controls.expectedKaratType.setValue(null);
    this.aurumServiceForm.controls.alloyQuantity.setValue(null);
    this.aurumServiceForm.controls.addedAlloy.setValue(null);
    this.aurumServiceForm.controls.rate.setValue(this.selectedServiceCharge);

    const gramValue = this.aurumServiceForm.controls.weight.value ? +this.aurumServiceForm.controls.weight.value : 0;
    // doing some math
    const voriValue = +(gramValue / VORI_TO_GRAM);
    const anaValue = +(voriValue % 1).toFixed(7) * 16;
    const rottiValue = +(anaValue % 1).toFixed(7) * 6;
    const pointValue = +(rottiValue % 1).toFixed(7) * 10;

    this.aurumServiceForm.controls.weightVori.setValue(Math.floor(voriValue));
    this.aurumServiceForm.controls.weightAna.setValue(Math.floor(anaValue));
    this.aurumServiceForm.controls.weightRotti.setValue(Math.floor(rottiValue));
    this.aurumServiceForm.controls.weightPoint.setValue(Math.floor(pointValue));

    const serviceTypeTemp = this.aurumServiceForm.controls.serviceType.value;
    if (serviceTypeTemp) {
      if (gramValue <= 116) {
        this.selectedServiceCharge = this.rateTypePriceMap.get(serviceTypeTemp);
        this.aurumServiceForm.controls.rate.setValue(this.selectedServiceCharge);
      } else {
        const extraWeightToCharge = +(gramValue % 116).toFixed(2);
        this.selectedServiceCharge = this.rateTypePriceMap.get(serviceTypeTemp) + extraWeightToCharge;
        this.aurumServiceForm.controls.rate.setValue(this.selectedServiceCharge);
      }
    }
  }

  onKaratTypeChange(event) {
    if (this.aurumServiceForm.controls.serviceType.value === 'Calculated Melting') {
      const selectedKaratParcent = this.karatTypePercentMap.get(this.aurumServiceForm.controls.karatType.value);
      if (this.aurumServiceForm.controls.expectedKaratType.value) {
        const expectedKaratParcent = this.karatTypePercentMap.get(this.aurumServiceForm.controls.expectedKaratType.value);
        this.karatParcentDifference = selectedKaratParcent - expectedKaratParcent;
        const gramValue = this.aurumServiceForm.controls.weight.value ? +this.aurumServiceForm.controls.weight.value : 0;
        const addedAlloyWeight = (gramValue * this.karatParcentDifference) / 100;
        this.aurumServiceForm.controls.alloyQuantity.setValue(addedAlloyWeight.toFixed(2));

        if (+this.karatParcentDifference <= 0) {
          this.aurumServiceForm.controls.karatType.setErrors({ percentDiffError: true });
          this.aurumServiceForm.controls.expectedKaratType.setErrors({ percentDiffError: true });
        }
      }
    }
  }

  onExpectedKaratTypeChange(event) {
    const expectedKaratParcent = this.karatTypePercentMap.get(this.aurumServiceForm.controls.expectedKaratType.value);
    if (this.aurumServiceForm.controls.karatType.value) {
      const selectedKaratParcent = this.karatTypePercentMap.get(this.aurumServiceForm.controls.karatType.value);
      this.karatParcentDifference = selectedKaratParcent - expectedKaratParcent;
      const gramValue = this.aurumServiceForm.controls.weight.value ? +this.aurumServiceForm.controls.weight.value : 0;
      const addedAlloyWeight = (gramValue * this.karatParcentDifference) / 100;
      this.aurumServiceForm.controls.alloyQuantity.setValue(addedAlloyWeight.toFixed(2));

      if (+this.karatParcentDifference <= 0) {
        this.aurumServiceForm.controls.karatType.setErrors({ percentDiffError: true });
        this.aurumServiceForm.controls.expectedKaratType.setErrors({ percentDiffError: true });
      }
    }
  }

  onAddedAlloyChange(event) {
    const alloyRate = this.rateTypePriceMap.get(event);
    if (this.aurumServiceForm.controls.alloyQuantity.value) {
      const alloyPrice = alloyRate * +this.aurumServiceForm.controls.alloyQuantity.value;
      this.aurumServiceForm.controls.rate.setValue(this.selectedServiceCharge + alloyPrice);
    }
  }

  // ****************************** WEIGHT CALCULATION ****************************** END

  ngOnDestroy() {
    // this.eventManager.destroy(this.eventSubscriber);
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
    (Object as any).values(formGroup.controls).forEach(control => {
      control.markAsDirty();
      control.markAllAsTouched();
      if (control.controls) {
        control.controls.forEach(ctrl => this.markFormGroupAsTouched(ctrl));
      }
    });
  }

  printPage() {
    window.print();
  }
}
