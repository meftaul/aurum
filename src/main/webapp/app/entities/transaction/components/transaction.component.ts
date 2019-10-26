import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { ICustomer } from 'app/shared/model/customer.model';
import { TransactionService } from '../services/service-api/transaction.service';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { CustomerService } from 'app/entities/customer/customer.service';
import { IVoucher, Voucher } from 'app/shared/model/voucher.model';
import { IAurumService, AurumService } from 'app/shared/model/aurum-service.model';

@Component({
  selector: 'jhi-aurum-transaction',
  templateUrl: './transaction.component.html',
  styleUrls: ['./transaction.component.scss']
})
export class TransactionComponent implements OnInit, OnDestroy {
  customerID: string;
  customer: ICustomer;
  // customer: ICustomer = { 'id': 1, 'firstName': 'Garnett', 'lastName': 'asdf', 'phone': '123456789', 'email': 'asdf@mail.com', 'totalPoint': 1000 };
  voucherForm: FormGroup;
  aurumServiceForm: FormGroup;

  aurumServices: AurumService[] = [];
  calculateTotalAmount = 0;
  totalAmount = 0;
  payableTotalAmount = 0;

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

  serviceListColumns: string[] = ['index', 'serviceType', 'itemName', 'karetType', 'quantity', 'weight', 'rate', 'amount', 'action'];
  serviceList: any[] = [{ itemName: 'Anklet', quantity: 2, weightGm: 15, rate: 100, amount: 2000 }];

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
      voucherNo: ['', [Validators.required]],
      boxNumber: ['', [Validators.required]],
      deliveryDate: ['', [Validators.required]],
      calculatedTotalAmount: [0, [Validators.required]],
      disountAmount: [0, [Validators.required]],
      vat: [0, [Validators.required]],
      totalPayableAmount: [0, [Validators.required]]
      // status: ['', [Validators.required]],

      // amount: ['', [Validators.required]],
      // serviceType: ['', [Validators.required]],
      // itemName: ['', [Validators.required]],
      // karatType: ['', [Validators.required]],
      // rate: ['', [Validators.required]],
      // quantity: ['', [Validators.required]],
      // weight: ['', [Validators.required]],
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
    this.calculateTotalAmount = 0;
    this.aurumServiceForm.markAllAsTouched();
    if (this.aurumServiceForm.invalid) {
      this.jhiAlertService.warning('error');
      return;
    }
    const aurumServiceTemp = new AurumService();
    aurumServiceTemp.serviceType = this.aurumServiceForm.controls.serviceType.value;
    aurumServiceTemp.itemName = this.aurumServiceForm.controls.itemName.value;
    aurumServiceTemp.karetType = this.aurumServiceForm.controls.karatType.value;
    aurumServiceTemp.rate = this.aurumServiceForm.controls.rate.value;
    aurumServiceTemp.quantity = this.aurumServiceForm.controls.quantity.value;
    aurumServiceTemp.amount = +this.aurumServiceForm.controls.rate.value * +this.aurumServiceForm.controls.quantity.value;
    aurumServiceTemp.weight = this.aurumServiceForm.controls.weight.value;

    this.aurumServices = [aurumServiceTemp, ...this.aurumServices];
    if (this.aurumServices.length !== 0) {
      this.aurumServices.map(service => {
        this.calculateTotalAmount += service.amount;
        this.payableTotalAmount += service.amount;
      });
    }
  }

  editService() {}

  deleteService() {}

  onVatValueChange(event) {
    this.voucherForm.controls.vat.valueChanges.subscribe(value => {
      if (value) {
        this.totalAmount = this.calculateTotalAmount + +value;
        const discount = this.voucherForm.controls.disountAmount ? +this.voucherForm.controls.disountAmount : 0;
        this.payableTotalAmount = this.totalAmount - discount;
      } else {
        this.totalAmount = this.calculateTotalAmount + 0;
        const discount = this.voucherForm.controls.disountAmount ? +this.voucherForm.controls.disountAmount : 0;
        this.payableTotalAmount = this.totalAmount - discount;
      }
    });
  }

  onDiscountValueChange(event) {
    this.voucherForm.controls.disountAmount.valueChanges.subscribe(value => {
      if (value) {
        const discount = this.voucherForm.controls.disountAmount ? +this.voucherForm.controls.disountAmount : 0;
        this.payableTotalAmount = this.totalAmount - discount;
      } else {
        this.payableTotalAmount = this.totalAmount - 0;
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
    const cTotalAmount = 0;
    // this.aurumServices.map(service => {
    //   cTotalAmount += service.amount;
    // })
    voucherTemp.boxNumber = this.voucherForm.controls.boxNumber.value;
    voucherTemp.calculatedTotalAmount = cTotalAmount;
    voucherTemp.disountAmount = this.voucherForm.controls.disountAmount.value;
    voucherTemp.vat = this.voucherForm.controls.vat.value;
    voucherTemp.totalPayableAmount = cTotalAmount + +this.voucherForm.controls.vat.value - +this.voucherForm.controls.disountAmount.value;
  }

  cancel() {}

  resetForm() {}

  ngOnDestroy() {
    // this.eventManager.destroy(this.eventSubscriber);
  }
}
