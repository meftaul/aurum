import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AurumSharedModule } from 'app/shared/shared.module';
import { TransactionHistoryComponent } from './transaction-history.component';
import { TransactionHistoryDetailComponent } from './transaction-history-detail.component';
import { TransactionHistoryUpdateComponent } from './transaction-history-update.component';
import { TransactionHistoryDeleteDialogComponent } from './transaction-history-delete-dialog.component';
import { transactionHistoryRoute } from './transaction-history.route';

@NgModule({
  imports: [AurumSharedModule, RouterModule.forChild(transactionHistoryRoute)],
  declarations: [
    TransactionHistoryComponent,
    TransactionHistoryDetailComponent,
    TransactionHistoryUpdateComponent,
    TransactionHistoryDeleteDialogComponent,
  ],
  entryComponents: [TransactionHistoryDeleteDialogComponent],
})
export class AurumTransactionHistoryModule {}
