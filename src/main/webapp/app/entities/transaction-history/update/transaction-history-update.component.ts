import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { TransactionStatus } from 'app/entities/enumerations/transaction-status.model';
import { ITransactionHistory } from '../transaction-history.model';
import { TransactionHistoryService } from '../service/transaction-history.service';
import { TransactionHistoryFormGroup, TransactionHistoryFormService } from './transaction-history-form.service';

@Component({
  selector: 'jhi-transaction-history-update',
  templateUrl: './transaction-history-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TransactionHistoryUpdateComponent implements OnInit {
  isSaving = false;
  transactionHistory: ITransactionHistory | null = null;
  transactionStatusValues = Object.keys(TransactionStatus);

  protected transactionHistoryService = inject(TransactionHistoryService);
  protected transactionHistoryFormService = inject(TransactionHistoryFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TransactionHistoryFormGroup = this.transactionHistoryFormService.createTransactionHistoryFormGroup();

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
    const transactionHistory: any = this.transactionHistoryFormService.getTransactionHistory(this.editForm);
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
