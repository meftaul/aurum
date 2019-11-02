import { Component, OnInit, OnDestroy } from '@angular/core';
import { Customer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer/customer.service';
import { NgbActiveModal, NgbModalRef, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { ActivatedRoute, Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'jhi-customer-form-dialog',
  templateUrl: './customer.form.component.html'
})
export class CustomerFormDialogComponent implements OnInit {
  customer: Customer;
  customerForm: FormGroup;

  constructor(
    protected customerService: CustomerService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit(): void {
    this.prepareCustomerForm(new Customer());
  }

  prepareCustomerForm(customerData: Customer) {
    this.customerForm = this.formBuilder.group({
      firstName: [customerData.firstName, [Validators.required]],
      lastName: [customerData.lastName],
      phone: [customerData.phone, [Validators.required]],
      email: [customerData.email, [Validators.required]],
      address: [customerData.address]
      // rewardPoints: [customerData.rewardPoints, [Validators.required]],
    });
  }

  clear() {
    this.activeModal.dismiss('cancel');
  }

  saveCustomer() {
    let customer = new Customer();
    customer = this.customerForm.value;
    this.customerService.create(customer).subscribe(response => {
      /* eslint-disable no-console */
      console.log(response.body);
      /* eslint-enable no-console */
      this.eventManager.broadcast({
        name: 'customerListModification',
        content: 'Create a customer'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-customer-delete-popup',
  template: ''
})
export class CustomerFormPopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ customer }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(CustomerFormDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.customer = customer;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/entity/transactions']);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/entity/transactions']);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
