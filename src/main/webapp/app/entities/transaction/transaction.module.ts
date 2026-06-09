import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';

import { TransactionComponent } from './components/transaction.component';
import { TRANSACTION_ROUTE } from './transaction.routes';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
  imports: [SharedModule, FormsModule, ReactiveFormsModule, RouterModule.forChild(TRANSACTION_ROUTE)],
  declarations: [TransactionComponent],
  providers: [NgbActiveModal],
})
export class AurumTransactionModule {}
