import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { TransactionHistoryFormService, TransactionHistoryFormGroup } from './transaction-history-form.service';
import { ITransactionHistory } from '../transaction-history.model';
import { TransactionHistoryService } from '../service/transaction-history.service';
import { TransactionStatus } from 'app/entities/enumerations/transaction-status.model';

@Component({
  selector: 'jhi-transaction-history-update',
  templateUrl: './transaction-history-update.component.html',
})
export class TransactionHistoryUpdateComponent implements OnInit {
  isSaving = false;
  transactionHistory: ITransactionHistory | null = null;
  transactionStatusValues = Object.keys(TransactionStatus);

  editForm: TransactionHistoryFormGroup = this.transactionHistoryFormService.createTransactionHistoryFormGroup();

  constructor(
    protected transactionHistoryService: TransactionHistoryService,
    protected transactionHistoryFormService: TransactionHistoryFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transactionHistory }) => {
      this.transactionHistory = transactionHistory;
      if (transactionHistory) {
        this.updateForm(transactionHistory);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const transactionHistory = this.transactionHistoryFormService.getTransactionHistory(this.editForm);
    if (transactionHistory.id !== null) {
      this.subscribeToSaveResponse(this.transactionHistoryService.update(transactionHistory));
    } else {
      this.subscribeToSaveResponse(this.transactionHistoryService.create(transactionHistory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransactionHistory>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(transactionHistory: ITransactionHistory): void {
    this.transactionHistory = transactionHistory;
    this.transactionHistoryFormService.resetForm(this.editForm, transactionHistory);
  }
}
