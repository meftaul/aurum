import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Customer } from './../../services/domain/customer.models';

@Component({
  selector: 'jhi-aurum-customer-form',
  templateUrl: './customer.form.component.html'
})
export class CustomerFormComponent implements OnInit, OnDestroy {
  @Input() customerId: number;
  customerForm: FormGroup;

  constructor(private formBuilder: FormBuilder) {}

  ngOnInit() {
    this.prepareCustomerForm(new Customer());
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

  editCustomer() {}

  saveCustomer() {}

  ngOnDestroy() {}
}
