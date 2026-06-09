import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITransactionHistory } from 'app/shared/model/transaction-history.model';
import { TransactionHistoryService } from './transaction-history.service';

@Component({
  selector: 'jhi-transaction-history-delete-dialog',
  templateUrl: './transaction-history-delete-dialog.component.html'
})
export class TransactionHistoryDeleteDialogComponent {
  transactionHistory: ITransactionHistory;

  constructor(
    protected transactionHistoryService: TransactionHistoryService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.transactionHistoryService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'transactionHistoryListModification',
        content: 'Deleted an transactionHistory'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-transaction-history-delete-popup',
  template: ''
})
export class TransactionHistoryDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ transactionHistory }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(TransactionHistoryDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.transactionHistory = transactionHistory;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/transaction-history', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/transaction-history', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
