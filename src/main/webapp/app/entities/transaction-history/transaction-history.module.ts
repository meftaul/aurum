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
import { MatCheckboxModule } from '@angular/material/checkbox';

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
    MatSelectModule,
    MatCheckboxModule,
    RouterModule.forChild(ENTITY_STATES)
  ],
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
