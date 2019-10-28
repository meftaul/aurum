import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { ICustomer } from 'app/shared/model/customer.model';
import { TransactionService } from '../services/service-api/transaction.service';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { CustomerService } from 'app/entities/customer/customer.service';
import { Voucher } from 'app/shared/model/voucher.model';
import { AurumService } from 'app/shared/model/aurum-service.model';
import * as moment from 'moment';
import { VoucherStatus } from 'app/shared/model/enumerations/voucher-status.model';

@Component({
  selector: 'jhi-aurum-transaction',
  templateUrl: './transaction.component.html',
  styleUrls: ['./transaction.component.scss']
})
export class TransactionComponent implements OnInit, OnDestroy {
  customerID: string;
  customer: ICustomer;
  voucherForm: FormGroup;
  aurumServiceForm: FormGroup;

  aurumServices: AurumService[] = [];
  calculateTotalAmount = 0;
  totalAmount = 0;
  payableTotalAmount = 0;
  amountDue = 0;

  eventSubscriber: Subscription;

  displayedBillingInfoColumns: string[] = ['index', 'name', 'weight', 'symbol'];
  billingInfos: any[] = [{ name: 'Hydrogen', weight: 1.0079, symbol: 'H' }];

  itemNames: any[] = [
    { value: 'Gold X-Ray', viewValue: 'Gold X-Ray' },
    { value: '1 Gold Check (Mark)', viewValue: '1 Gold Check (Mark)' },
    { value: 'Anklet', viewValue: 'Anklet' },
    { value: 'Ayesti', viewValue: 'Ayesti' },
    { value: 'Baju', viewValue: 'Baju' }
  ];

  voucherStatus: any[] = [
    { value: 'PAID', viewValue: 'PAID' },
    { value: 'DUE', viewValue: 'DUE' },
    { value: 'CANCEL', viewValue: 'CANCEL' }
  ];

  serviceListColumns: string[] = ['index', 'serviceType', 'itemName', 'karatType', 'quantity', 'weight', 'rate', 'amount', 'action'];

  constructor(
    private formBuilder: FormBuilder,
    protected jhiAlertService: JhiAlertService,
    private transactionService: TransactionService,
    protected customerService: CustomerService,
    protected eventManager: JhiEventManager
  ) {}

  ngOnInit() {
    this.prepareAurumServiceForm();
    this.prepareVoucherForm();
    // this.prepareCustomerForm(new Customer());
  }

  prepareVoucherForm() {
    this.voucherForm = this.formBuilder.group({
      voucherNo: [''],
      boxNumber: ['', [Validators.required]],
      deliveryDate: ['', [Validators.required]],
      calculatedTotalAmount: [0],
      disountAmount: [0, [Validators.required]],
      vat: [0, [Validators.required]],
      totalPayableAmount: [0]
      // status: ['', [Validators.required]],
    });
  }

  prepareAurumServiceForm() {
    this.aurumServiceForm = this.formBuilder.group({
      serviceType: ['', [Validators.required]],
      itemName: ['', [Validators.required]],
      karatType: ['', [Validators.required]],
      rate: ['', [Validators.required]],
      quantity: ['', [Validators.required]],
      weight: ['', [Validators.required]]
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

    this.aurumServices = [aurumServiceTemp, ...this.aurumServices];
    this.calculateTotalServiceCharge(this.aurumServices);
    this.resetServiceForm();
  }

  editService() {}

  deleteService(index) {
    this.aurumServices.splice(index, 1);
    this.aurumServices = [...this.aurumServices];
    this.calculateTotalServiceCharge(this.aurumServices);
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

  makePayment() {
    this.aurumServiceForm.markAllAsTouched();
    if (this.voucherForm.invalid || this.aurumServices.length === 0) {
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
    voucherTemp.aurumServices = this.aurumServices;
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
    this.aurumServices = [];
    this.calculateTotalAmount = 0;
    this.totalAmount = 0;
    this.payableTotalAmount = 0;
    this.amountDue = 0;
    this.customer = null;
  }

  resetServiceForm() {
    this.aurumServiceForm.reset();
  }

  ngOnDestroy() {
    // this.eventManager.destroy(this.eventSubscriber);
  }
}
