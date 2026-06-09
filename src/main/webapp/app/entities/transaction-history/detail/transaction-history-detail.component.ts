import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { ITransactionHistory } from '../transaction-history.model';

@Component({
  selector: 'jhi-transaction-history-detail',
  templateUrl: './transaction-history-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class TransactionHistoryDetailComponent {
  transactionHistory = input<ITransactionHistory | null>(null);

  previousState(): void {
    window.history.back();
  }
}
