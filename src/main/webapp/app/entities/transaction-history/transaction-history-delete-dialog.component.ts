import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITransactionHistory } from 'app/shared/model/transaction-history.model';
import { TransactionHistoryService } from './transaction-history.service';

@Component({
  templateUrl: './transaction-history-delete-dialog.component.html',
})
export class TransactionHistoryDeleteDialogComponent {
  transactionHistory?: ITransactionHistory;

  constructor(
    protected transactionHistoryService: TransactionHistoryService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.transactionHistoryService.delete(id).subscribe(() => {
      this.eventManager.broadcast('transactionHistoryListModification');
      this.activeModal.close();
    });
  }
}
