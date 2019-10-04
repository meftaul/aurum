import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { Transaction } from './../services/domain/transaction.models';
import { Customer } from './../services/domain/customer.models';

@Component({
  selector: 'jhi-aurum-transaction',
  templateUrl: './transaction.component.html',
  styleUrls: ['./transaction.component.scss']
})
export class TransactionComponent implements OnInit, OnDestroy {
  customerID: string;
  transactionForm: FormGroup;
  customerForm: FormGroup;

  displayedBillingInfoColumns: string[] = ['index', 'name', 'weight', 'symbol'];
  billingInfos: any[] = [{ name: 'Hydrogen', weight: 1.0079, symbol: 'H' }];

  itemNames: any[] = [
    { value: 'Gold X-Ray', viewValue: 'Gold X-Ray' },
    { value: '1 Gold Check (Mark)', viewValue: '1 Gold Check (Mark)' },
    { value: 'Anklet', viewValue: 'Anklet' },
    { value: 'Ayesti', viewValue: 'Ayesti' },
    { value: 'Baju', viewValue: 'Baju' }
  ];

  serviceListColumns: string[] = ['index', 'itemName', 'quantity', 'weightGm', 'rate', 'amount', 'action'];
  serviceList: any[] = [{ itemName: 'Anklet', quantity: 2, weightGm: 15, rate: 100, amount: 2000 }];

  constructor(private formBuilder: FormBuilder) {}

  ngOnInit() {
    this.prepareTransactionForm(new Transaction());
    this.prepareCustomerForm(new Customer());
  }

  prepareTransactionForm(transactionData: Transaction) {
    this.transactionForm = this.formBuilder.group({
      voucherNo: [transactionData.voucherNo, [Validators.required]],
      boxNo: [transactionData.boxNo, [Validators.required]],
      karatType: [transactionData.karatType, [Validators.required]],
      deliveryDate: [transactionData.deliveryDate, [Validators.required]]
    });
  }

  prepareCustomerForm(customerData: Customer) {
    this.customerForm = this.formBuilder.group({
      name: [customerData.name, [Validators.required]],
      mobileNumber: [customerData.mobileNumber, [Validators.required]],
      address1: [customerData.address1, [Validators.required]],
      address2: [customerData.address2]
      // rewardPoints: [customerData.rewardPoints, [Validators.required]],
    });
  }

  searchCustomer() {
    /* eslint-disable no-console */
    console.log(this.customerID);
    /* eslint-enable no-console */
  }

  editCustomer() {}

  saveCustomer() {}

  ngOnDestroy() {}
}
