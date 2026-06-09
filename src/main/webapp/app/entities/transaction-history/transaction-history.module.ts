import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TransactionHistoryComponent } from './list/transaction-history.component';
import { TransactionHistoryDetailComponent } from './detail/transaction-history-detail.component';
import { TransactionHistoryUpdateComponent } from './update/transaction-history-update.component';
import { TransactionHistoryDeleteDialogComponent } from './delete/transaction-history-delete-dialog.component';
import { TransactionHistoryRoutingModule } from './route/transaction-history-routing.module';

@NgModule({
  imports: [SharedModule, TransactionHistoryRoutingModule],
  declarations: [
    TransactionHistoryComponent,
    TransactionHistoryDetailComponent,
    TransactionHistoryUpdateComponent,
    TransactionHistoryDeleteDialogComponent,
  ],
})
export class TransactionHistoryModule {}
