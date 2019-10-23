import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITransactionHistory } from 'app/shared/model/transaction-history.model';

@Component({
  selector: 'jhi-transaction-history-detail',
  templateUrl: './transaction-history-detail.component.html'
})
export class TransactionHistoryDetailComponent implements OnInit {
  transactionHistory: ITransactionHistory;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ transactionHistory }) => {
      this.transactionHistory = transactionHistory;
    });
  }

  previousState() {
    window.history.back();
  }
}
