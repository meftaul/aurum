import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITransactionHistory, NewTransactionHistory } from '../transaction-history.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITransactionHistory for edit and NewTransactionHistoryFormGroupInput for create.
 */
type TransactionHistoryFormGroupInput = ITransactionHistory | PartialWithRequiredKeyOf<NewTransactionHistory>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITransactionHistory | NewTransactionHistory> = Omit<T, 'dateCreated'> & {
  dateCreated?: string | null;
};

type TransactionHistoryFormRawValue = FormValueOf<ITransactionHistory>;

type NewTransactionHistoryFormRawValue = FormValueOf<NewTransactionHistory>;

type TransactionHistoryFormDefaults = Pick<NewTransactionHistory, 'id' | 'dateCreated'>;

type TransactionHistoryFormGroupContent = {
  id: FormControl<TransactionHistoryFormRawValue['id'] | NewTransactionHistory['id']>;
  voucherNo: FormControl<TransactionHistoryFormRawValue['voucherNo']>;
  amount: FormControl<TransactionHistoryFormRawValue['amount']>;
  dateCreated: FormControl<TransactionHistoryFormRawValue['dateCreated']>;
  tag: FormControl<TransactionHistoryFormRawValue['tag']>;
  customerId: FormControl<TransactionHistoryFormRawValue['customerId']>;
  addedBy: FormControl<TransactionHistoryFormRawValue['addedBy']>;
};

export type TransactionHistoryFormGroup = FormGroup<TransactionHistoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TransactionHistoryFormService {
  createTransactionHistoryFormGroup(transactionHistory: TransactionHistoryFormGroupInput = { id: null }): TransactionHistoryFormGroup {
    const transactionHistoryRawValue = this.convertTransactionHistoryToTransactionHistoryRawValue({
      ...this.getFormDefaults(),
      ...transactionHistory,
    });
    return new FormGroup<TransactionHistoryFormGroupContent>({
      id: new FormControl(
        { value: transactionHistoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      voucherNo: new FormControl(transactionHistoryRawValue.voucherNo, {
        validators: [Validators.required],
      }),
      amount: new FormControl(transactionHistoryRawValue.amount, {
        validators: [Validators.required],
      }),
      dateCreated: new FormControl(transactionHistoryRawValue.dateCreated, {
        validators: [Validators.required],
      }),
      tag: new FormControl(transactionHistoryRawValue.tag, {
        validators: [Validators.required],
      }),
      customerId: new FormControl(transactionHistoryRawValue.customerId, {
        validators: [Validators.required],
      }),
      addedBy: new FormControl(transactionHistoryRawValue.addedBy, {
        validators: [Validators.required],
      }),
    });
  }

  getTransactionHistory(form: TransactionHistoryFormGroup): ITransactionHistory | NewTransactionHistory {
    return this.convertTransactionHistoryRawValueToTransactionHistory(
      form.getRawValue() as TransactionHistoryFormRawValue | NewTransactionHistoryFormRawValue,
    );
  }

  resetForm(form: TransactionHistoryFormGroup, transactionHistory: TransactionHistoryFormGroupInput): void {
    const transactionHistoryRawValue = this.convertTransactionHistoryToTransactionHistoryRawValue({
      ...this.getFormDefaults(),
      ...transactionHistory,
    });
    form.reset(
      {
        ...transactionHistoryRawValue,
        id: { value: transactionHistoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TransactionHistoryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateCreated: currentTime,
    };
  }

  private convertTransactionHistoryRawValueToTransactionHistory(
    rawTransactionHistory: TransactionHistoryFormRawValue | NewTransactionHistoryFormRawValue,
  ): ITransactionHistory | NewTransactionHistory {
    return {
      ...rawTransactionHistory,
      dateCreated: dayjs(rawTransactionHistory.dateCreated, DATE_TIME_FORMAT),
    };
  }

  private convertTransactionHistoryToTransactionHistoryRawValue(
    transactionHistory: ITransactionHistory | (Partial<NewTransactionHistory> & TransactionHistoryFormDefaults),
  ): TransactionHistoryFormRawValue | PartialWithRequiredKeyOf<NewTransactionHistoryFormRawValue> {
    return {
      ...transactionHistory,
      dateCreated: transactionHistory.dateCreated ? transactionHistory.dateCreated.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
