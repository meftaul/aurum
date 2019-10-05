import { NgModule } from '@angular/core';

import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material';
import { MatTableModule } from '@angular/material/table';
import { MatSelectModule } from '@angular/material/select';

import { AurumSharedModule } from 'app/shared/shared.module';

import { CustomerFormComponent } from './customer.form.component';

@NgModule({
  imports: [
    AurumSharedModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatInputModule,
    MatFormFieldModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatTableModule,
    MatSelectModule
  ],
  declarations: [CustomerFormComponent],
  providers: [MatDatepickerModule],
  exports: [MatDatepickerModule, CustomerFormComponent]
})
export class AurumCustomerFormModule {}
