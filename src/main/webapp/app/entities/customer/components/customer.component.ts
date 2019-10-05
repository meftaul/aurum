import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'jhi-aurum-customer',
  templateUrl: './customer.component.html'
})
export class CustomerComponent implements OnInit, OnDestroy {
  constructor(private formBuilder: FormBuilder) {}

  ngOnInit() {}

  ngOnDestroy() {}
}
