import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

import { AurumSharedModule } from 'app/shared/shared.module';

import { TransactionComponent } from './components/transaction.component';
import { TRANSACTION_ROUTE } from './transaction.routes';

@NgModule({
  imports: [AurumSharedModule, RouterModule.forChild([TRANSACTION_ROUTE]), MatButtonModule, MatIconModule],
  declarations: [TransactionComponent]
})
export class AurumTransactionModule {}
