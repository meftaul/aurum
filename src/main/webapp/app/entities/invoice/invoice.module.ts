import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatTableModule } from '@angular/material/table';
import { MatSelectModule } from '@angular/material/select';
import { MatCheckboxModule } from '@angular/material/checkbox';

import { SharedModule } from 'app/shared/shared.module';
// import { InvoiceComponent } from './components/invoice.component';
import { INVOICE_ROUTE } from './invoice.routes';
import { InvoiceNewComponent } from './component-new/invoice.component';

@NgModule({
  imports: [
    SharedModule,
    RouterModule.forChild(INVOICE_ROUTE),
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatInputModule,
    MatFormFieldModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatTableModule,
    MatSelectModule,
    MatCheckboxModule
  ],
  declarations: [InvoiceNewComponent],
  providers: [MatDatepickerModule]
})
export class AurumInvoiceModule {}
