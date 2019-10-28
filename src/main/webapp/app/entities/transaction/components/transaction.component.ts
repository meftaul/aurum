import { Component, OnInit, OnDestroy, ChangeDetectionStrategy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { ICustomer } from 'app/shared/model/customer.model';
import { TransactionService } from '../services/service-api/transaction.service';
import { VOUCHER_STATUS, SERVICE_LIST_COLUMNS, AURUM_SERVICE_LIST } from '../services/domain/transaction.models';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { CustomerService } from 'app/entities/customer/customer.service';
import { Voucher } from 'app/shared/model/voucher.model';
import { AurumService } from 'app/shared/model/aurum-service.model';
import * as moment from 'moment';
import { VoucherStatus } from 'app/shared/model/enumerations/voucher-status.model';
import { KaratService } from 'app/entities/karat/karat.service';
import { RateService } from 'app/entities/rate/rate.service';

const VORI_TO_GRAM = 11.6638125;
const ANA_TO_GRAM = 0.72898828125;
const ROTTI_TO_GRAM = 0.121498046875;
const POINT_TO_GRAM = 0.0121498046875;

@Component({
  selector: 'jhi-aurum-transaction',
  templateUrl: './transaction.component.html',
  styleUrls: ['./transaction.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TransactionComponent implements OnInit, OnDestroy {
  customerID: string;
  customer: ICustomer;
  voucherForm: FormGroup;
  aurumServiceForm: FormGroup;

  aurumServiceList: AurumService[] = [];
  calculateTotalAmount = 0;
  totalAmount = 0;
  payableTotalAmount = 0;
  amountDue = 0;

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
  karatList: any[] = [];
  serviceListColumns: string[] = SERVICE_LIST_COLUMNS;

  constructor(
    private formBuilder: FormBuilder,
    protected jhiAlertService: JhiAlertService,
    private transactionService: TransactionService,
    protected customerService: CustomerService,
    protected karatService: KaratService,
    protected rateService: RateService,
    protected eventManager: JhiEventManager
  ) {}

  ngOnInit() {
    this.prepareAurumServiceForm();
    this.prepareVoucherForm();
    // this.prepareCustomerForm(new Customer());

    this.fetchKaratList();
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
      this.jhiAlertService.warning('error');
      return;
    }

    const aurumServiceTemp = new AurumService();
    aurumServiceTemp.serviceType = this.aurumServiceForm.controls.serviceType.value;
    aurumServiceTemp.itemName = this.aurumServiceForm.controls.itemName.value;
    aurumServiceTemp.karatType = this.aurumServiceForm.controls.karatType.value;
    aurumServiceTemp.rate = this.aurumServiceForm.controls.rate.value;
    aurumServiceTemp.quantity = this.aurumServiceForm.controls.quantity.value;
    aurumServiceTemp.amount = +this.aurumServiceForm.controls.rate.value * +this.aurumServiceForm.controls.quantity.value;
    aurumServiceTemp.weight = this.aurumServiceForm.controls.weight.value;

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
    this.rateService.query({ rateType: event }).subscribe(data => {
      /* eslint-disable no-console */
      console.log(data);
      /* eslint-enable no-console */
    });
  }

  resetServiceForm() {
    this.aurumServiceForm.reset();
  }
  // ****************************** AURUM SERVICE ****************************** END

  // ****************************** CALCULATE BILL ****************************** START
  onVatValueChange(event) {
    this.voucherForm.controls.vat.valueChanges.subscribe(value => {
      if (value) {
        this.totalAmount = this.calculateTotalAmount + +value;
        const discount = this.voucherForm.controls.disountAmount.value ? +this.voucherForm.controls.disountAmount.value : 0;
        this.payableTotalAmount = this.totalAmount - discount;
        this.amountDue = this.totalAmount - discount;
      } else {
        this.totalAmount = this.calculateTotalAmount + 0;
        const discount = this.voucherForm.controls.disountAmount ? +this.voucherForm.controls.disountAmount : 0;
        this.payableTotalAmount = this.totalAmount - discount;
        this.amountDue = this.totalAmount - discount;
      }
    });
  }

  onDiscountValueChange(event) {
    this.voucherForm.controls.disountAmount.valueChanges.subscribe(value => {
      if (value) {
        const discount = this.voucherForm.controls.disountAmount.value ? +this.voucherForm.controls.disountAmount.value : 0;
        this.payableTotalAmount = this.totalAmount - discount;
        this.amountDue = this.totalAmount - discount;
      } else {
        this.payableTotalAmount = this.totalAmount - 0;
        this.amountDue = this.totalAmount - 0;
      }
    });
  }

  onTotalPayableValueChange(event) {
    this.voucherForm.controls.totalPayableAmount.valueChanges.subscribe(value => {
      if (value) {
        this.amountDue = this.payableTotalAmount - +value;
      } else {
        this.amountDue = this.payableTotalAmount - 0;
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
    this.aurumServiceForm.markAllAsTouched();
    if (this.voucherForm.invalid || this.aurumServiceList.length === 0) {
      this.jhiAlertService.warning('error');
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
    voucherTemp.addedBy = 'admin';

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
          this.karatList = [{ value: karat.karatType, viewValue: karat.karatType + '(' + karat.purityPercent + ')' }, ...this.karatList];
        });
      }
    });
  }

  // ****************************** WEIGHT CALCULATION ****************************** START
  onWeightValueChange(event) {
    this.aurumServiceForm.controls.weight.valueChanges.subscribe(value => {
      const wValue = +this.aurumServiceForm.controls.weight.value;
      this.aurumServiceForm.controls.weightVori.setValue(wValue / VORI_TO_GRAM);
      this.aurumServiceForm.controls.weightAna.setValue(wValue / ANA_TO_GRAM);
      this.aurumServiceForm.controls.weightRotti.setValue(wValue / ROTTI_TO_GRAM);
      this.aurumServiceForm.controls.weightPoint.setValue(wValue / POINT_TO_GRAM);
    });
  }

  onWeightVoriValueChange(event) {
    // this.aurumServiceForm.controls.weightVori.valueChanges.subscribe(value => {
    //   const vValue = +this.aurumServiceForm.controls.weightVori.value;
    //   this.aurumServiceForm.controls.weight.setValue(vValue * VORI_TO_GRAM);
    //   this.aurumServiceForm.controls.weightAna.setValue(vValue / ANA_TO_GRAM);
    //   this.aurumServiceForm.controls.weightRotti.setValue(vValue / ROTTI_TO_GRAM);
    //   this.aurumServiceForm.controls.weightPoint.setValue(vValue / POINT_TO_GRAM);
    // });
  }

  onWeightAnaValueChange(event) {}

  onWeightRottiValueChange(event) {}

  onWeightPointValueChange(event) {}
  // ****************************** WEIGHT CALCULATION ****************************** END

  ngOnDestroy() {
    // this.eventManager.destroy(this.eventSubscriber);
  }
}
