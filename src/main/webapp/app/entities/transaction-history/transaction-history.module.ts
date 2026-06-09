import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AurumSharedModule } from 'app/shared/shared.module';
import { TransactionHistoryComponent } from './transaction-history.component';
import { TransactionHistoryDetailComponent } from './transaction-history-detail.component';
import { TransactionHistoryUpdateComponent } from './transaction-history-update.component';
import {
  TransactionHistoryDeletePopupComponent,
  TransactionHistoryDeleteDialogComponent
} from './transaction-history-delete-dialog.component';
import { transactionHistoryRoute, transactionHistoryPopupRoute } from './transaction-history.route';

const ENTITY_STATES = [...transactionHistoryRoute, ...transactionHistoryPopupRoute];

@NgModule({
  imports: [AurumSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    TransactionHistoryComponent,
    TransactionHistoryDetailComponent,
    TransactionHistoryUpdateComponent,
    TransactionHistoryDeleteDialogComponent,
    TransactionHistoryDeletePopupComponent
  ],
  entryComponents: [TransactionHistoryDeleteDialogComponent]
})
export class AurumTransactionHistoryModule {}
