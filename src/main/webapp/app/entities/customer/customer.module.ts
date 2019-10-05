import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';

import { AurumSharedModule } from 'app/shared/shared.module';

import { CustomerComponent } from './components/customer.component';

@NgModule({
  imports: [
    AurumSharedModule,
    // RouterModule.forChild([ CUSTOMER_ROUTE]),
    MatButtonModule,
    MatIconModule,
    MatCardModule
  ],
  declarations: [CustomerComponent],
  providers: [],
  exports: [CustomerComponent]
})
export class AurumCustomerModule {}
