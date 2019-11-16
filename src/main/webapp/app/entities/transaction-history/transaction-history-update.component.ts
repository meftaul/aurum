import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ITransactionHistory, TransactionHistory } from 'app/shared/model/transaction-history.model';
import { TransactionHistoryService } from './transaction-history.service';

@Component({
  selector: 'jhi-transaction-history-update',
  templateUrl: './transaction-history-update.component.html'
})
export class TransactionHistoryUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    voucherNo: [null, [Validators.required]],
    amount: [null, [Validators.required]],
    dateCreated: [null, [Validators.required]],
    tag: [null, [Validators.required]],
    customerId: [null, [Validators.required]],
    addedBy: [null, [Validators.required]]
  });

  constructor(
    protected transactionHistoryService: TransactionHistoryService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ transactionHistory }) => {
      this.updateForm(transactionHistory);
    });
  }

  updateForm(transactionHistory: ITransactionHistory) {
    this.editForm.patchValue({
      id: transactionHistory.id,
      voucherNo: transactionHistory.voucherNo,
      amount: transactionHistory.amount,
      dateCreated: transactionHistory.dateCreated != null ? transactionHistory.dateCreated.format(DATE_TIME_FORMAT) : null,
      tag: transactionHistory.tag,
      customerId: transactionHistory.customerId,
      addedBy: transactionHistory.addedBy
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const transactionHistory = this.createFromForm();
    if (transactionHistory.id !== undefined) {
      this.subscribeToSaveResponse(this.transactionHistoryService.update(transactionHistory));
    } else {
      this.subscribeToSaveResponse(this.transactionHistoryService.create(transactionHistory));
    }
  }

  private createFromForm(): ITransactionHistory {
    return {
      ...new TransactionHistory(),
      id: this.editForm.get(['id']).value,
      voucherNo: this.editForm.get(['voucherNo']).value,
      amount: this.editForm.get(['amount']).value,
      dateCreated:
        this.editForm.get(['dateCreated']).value != null ? moment(this.editForm.get(['dateCreated']).value, DATE_TIME_FORMAT) : undefined,
      tag: this.editForm.get(['tag']).value,
      customerId: this.editForm.get(['customerId']).value,
      addedBy: this.editForm.get(['addedBy']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransactionHistory>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
