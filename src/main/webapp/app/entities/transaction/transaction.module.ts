import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

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

import { TransactionComponent } from './components/transaction.component';
import { TRANSACTION_ROUTE } from './transaction.routes';

@NgModule({
  imports: [
    AurumSharedModule,
    RouterModule.forChild([TRANSACTION_ROUTE]),
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
  declarations: [TransactionComponent],
  providers: [MatDatepickerModule]
})
export class AurumTransactionModule {}
