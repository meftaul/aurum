import { Component, OnInit, OnDestroy, ChangeDetectionStrategy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { ICustomer } from 'app/shared/model/customer.model';
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

const VORI_TO_GRAM = 11.6638125; // = 1 vori
// const ANA_TO_GRAM = 0.72898828125;
// const ROTTI_TO_GRAM = 0.121498046875;
// const POINT_TO_GRAM = 0.0121498046875;

@Component({
  selector: 'jhi-aurum-transaction',
  templateUrl: './transaction.component.html',
  styleUrls: ['./transaction.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TransactionComponent implements OnInit, OnDestroy {
  account: Account;
  customerID: string;
  customer: ICustomer;
  voucherForm: FormGroup;
  aurumServiceForm: FormGroup;

  showBtnForCalculatedMelting = false;

  aurumServiceList: AurumService[] = [];
  calculateTotalAmount = 0;
  totalAmount = 0;
  payableTotalAmount = 0;
  amountDue = 0;
  selectedServiceCharge = 0;
  karatParcentDifference = 0;

  eventSubscriber: Subscription;

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

  constructor(
    private formBuilder: FormBuilder,
    protected jhiAlertService: JhiAlertService,
    private transactionService: TransactionService,
    protected customerService: CustomerService,
    protected karatService: KaratService,
    protected rateService: RateService,
    protected itemService: ItemService,
    private accountService: AccountService,
    protected eventManager: JhiEventManager
  ) {}

  ngOnInit() {
    this.prepareAurumServiceForm();
    this.prepareVoucherForm();
    // this.prepareCustomerForm(new Customer());

    this.fetchKaratList();
    this.fetchRateList();
    this.fetchItemList();

    this.accountService.identity().then((account: Account) => {
      this.account = account;
    });
  }

  // prepareCustomerForm(customerData: Customer) {
  //   this.customerForm = this.formBuilder.group({
  //     name: [customerData.name, [Validators.required]],
  //     mobileNumber: [customerData.mobileNumber, [Validators.required]],
  //     address1: [customerData.address1, [Validators.required]],
  //     address2: [customerData.address2]
  //     // rewardPoints: [customerData.rewardPoints, [Validators.required]],
  //   });
  // }

  // ******************** CUSTOMER ******************** START
  searchCustomer() {
    this.customerService.find(+this.customerID).subscribe(
      data => {
        this.customer = data.body;
      },
      error => {
        this.customer = null;
      }
    );
  }

  createCustomer() {
    let customerTemp: ICustomer;
    this.customerService.create(customerTemp).subscribe(data => {});
  }
  // ****************************** CUSTOMER ****************************** END

  // ****************************** AURUM SERVICE ****************************** START
  prepareAurumServiceForm() {
    this.aurumServiceForm = this.formBuilder.group({
      serviceType: ['', [Validators.required]],
      itemName: ['', [Validators.required]],
      karatType: ['', [Validators.required]],
      expectedKaratType: [''],
      addedAlloy: [''],
      alloyQuantity: [''],
      rate: ['', [Validators.required]],
      quantity: ['', [Validators.required]],
      weight: ['', [Validators.required]],

      // only for weight calculation
      weightVori: [''],
      weightAna: [''],
      weightRotti: [''],
      weightPoint: ['']
    });
  }

  addService() {
    this.aurumServiceForm.markAllAsTouched();
    if (this.aurumServiceForm.invalid) {
      this.jhiAlertService.warning('Form Invalid.');
      return;
    }

    const aurumServiceTemp = new AurumService();
    aurumServiceTemp.serviceType = this.aurumServiceForm.controls.serviceType.value;
    aurumServiceTemp.itemName = this.aurumServiceForm.controls.itemName.value;
    aurumServiceTemp.karatType = this.aurumServiceForm.controls.karatType.value;
    aurumServiceTemp.rate = this.aurumServiceForm.controls.rate.value;
    aurumServiceTemp.serviceCharge = this.selectedServiceCharge;
    aurumServiceTemp.quantity = this.aurumServiceForm.controls.quantity.value;
    aurumServiceTemp.amount = +this.aurumServiceForm.controls.rate.value * +this.aurumServiceForm.controls.quantity.value;
    aurumServiceTemp.weight = this.aurumServiceForm.controls.weight.value;

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
        this.payableTotalAmount += service.amount;
        this.amountDue += service.amount;
      });
    }
  }

  serviceTypeChange(event) {
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

  resetServiceForm() {
    this.selectedServiceCharge = 0;
    this.karatParcentDifference = 0;
    this.aurumServiceForm.reset();
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
    this.voucherForm.controls.totalPayableAmount.valueChanges.subscribe(value => {
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
      totalPayableAmount: [0]
    });
  }

  makePayment() {
    // this.aurumServiceForm.markAllAsTouched();
    if (this.aurumServiceList.length === 0) {
      this.jhiAlertService.warning('Invalid Data.');
      return;
    }

    const voucherTemp = new Voucher();
    voucherTemp.voucherNo = moment().toString();
    voucherTemp.customerId = +this.customerID;
    voucherTemp.boxNumber = this.voucherForm.controls.boxNumber.value;
    voucherTemp.calculatedTotalAmount = this.calculateTotalAmount;
    voucherTemp.disountAmount = this.voucherForm.controls.disountAmount.value;
    voucherTemp.vat = this.voucherForm.controls.vat.value;
    voucherTemp.totalPayableAmount = this.voucherForm.controls.totalPayableAmount.value;
    voucherTemp.aurumServices = this.aurumServiceList;
    voucherTemp.deliveryDate = this.voucherForm.controls.deliveryDate.value; // moment()
    if (this.payableTotalAmount === +this.voucherForm.controls.totalPayableAmount.value) voucherTemp.status = VoucherStatus.PAID;
    else voucherTemp.status = VoucherStatus.DUE;
    // voucherTemp.status = VoucherStatus.PAID;
    voucherTemp.addedBy = this.account.login;

    this.transactionService.create(voucherTemp).subscribe(data => {
      this.jhiAlertService.success('success');
      this.resetVoucherForm();
    });
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
    const gramValue = this.aurumServiceForm.controls.weight.value ? +this.aurumServiceForm.controls.weight.value.trim() : 0;
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
        const gramValue = this.aurumServiceForm.controls.weight.value ? +this.aurumServiceForm.controls.weight.value.trim() : 0;
        const addedAlloyWeight = (gramValue * this.karatParcentDifference) / 100;
        this.aurumServiceForm.controls.alloyQuantity.setValue(addedAlloyWeight.toFixed(2));
      }
    }
  }

  onExpectedKaratTypeChange(event) {
    const expectedKaratParcent = this.karatTypePercentMap.get(this.aurumServiceForm.controls.expectedKaratType.value);
    if (this.aurumServiceForm.controls.karatType.value) {
      const selectedKaratParcent = this.karatTypePercentMap.get(this.aurumServiceForm.controls.karatType.value);
      this.karatParcentDifference = selectedKaratParcent - expectedKaratParcent;
      const gramValue = this.aurumServiceForm.controls.weight.value ? +this.aurumServiceForm.controls.weight.value.trim() : 0;
      const addedAlloyWeight = (gramValue * this.karatParcentDifference) / 100;
      this.aurumServiceForm.controls.alloyQuantity.setValue(addedAlloyWeight.toFixed(2));
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
}
