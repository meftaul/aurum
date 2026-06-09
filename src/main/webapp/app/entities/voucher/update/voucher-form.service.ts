import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IVoucher, NewVoucher } from '../voucher.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVoucher for edit and NewVoucherFormGroupInput for create.
 */
type VoucherFormGroupInput = IVoucher | PartialWithRequiredKeyOf<NewVoucher>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IVoucher | NewVoucher> = Omit<T, 'dateCreated' | 'deliveryDate'> & {
  dateCreated?: string | null;
  deliveryDate?: string | null;
};

type VoucherFormRawValue = FormValueOf<IVoucher>;

type NewVoucherFormRawValue = FormValueOf<NewVoucher>;

type VoucherFormDefaults = Pick<NewVoucher, 'id' | 'dateCreated' | 'deliveryDate' | 'deliveryStatus'>;

type VoucherFormGroupContent = {
  id: FormControl<VoucherFormRawValue['id'] | NewVoucher['id']>;
  voucherNo: FormControl<VoucherFormRawValue['voucherNo']>;
  customerId: FormControl<VoucherFormRawValue['customerId']>;
  calculatedTotalAmount: FormControl<VoucherFormRawValue['calculatedTotalAmount']>;
  vat: FormControl<VoucherFormRawValue['vat']>;
  disountAmount: FormControl<VoucherFormRawValue['disountAmount']>;
  status: FormControl<VoucherFormRawValue['status']>;
  totalPayableAmount: FormControl<VoucherFormRawValue['totalPayableAmount']>;
  dateCreated: FormControl<VoucherFormRawValue['dateCreated']>;
  addedBy: FormControl<VoucherFormRawValue['addedBy']>;
  boxNumber: FormControl<VoucherFormRawValue['boxNumber']>;
  deliveryDate: FormControl<VoucherFormRawValue['deliveryDate']>;
  deliveryStatus: FormControl<VoucherFormRawValue['deliveryStatus']>;
};

export type VoucherFormGroup = FormGroup<VoucherFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VoucherFormService {
  createVoucherFormGroup(voucher: VoucherFormGroupInput = { id: null }): VoucherFormGroup {
    const voucherRawValue = this.convertVoucherToVoucherRawValue({
      ...this.getFormDefaults(),
      ...voucher,
    });
    return new FormGroup<VoucherFormGroupContent>({
      id: new FormControl(
        { value: voucherRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      voucherNo: new FormControl(voucherRawValue.voucherNo),
      customerId: new FormControl(voucherRawValue.customerId),
      calculatedTotalAmount: new FormControl(voucherRawValue.calculatedTotalAmount, {
        validators: [Validators.required],
      }),
      vat: new FormControl(voucherRawValue.vat),
      disountAmount: new FormControl(voucherRawValue.disountAmount),
      status: new FormControl(voucherRawValue.status, {
        validators: [Validators.required],
      }),
      totalPayableAmount: new FormControl(voucherRawValue.totalPayableAmount, {
        validators: [Validators.required],
      }),
      dateCreated: new FormControl(voucherRawValue.dateCreated),
      addedBy: new FormControl(voucherRawValue.addedBy, {
        validators: [Validators.required],
      }),
      boxNumber: new FormControl(voucherRawValue.boxNumber),
      deliveryDate: new FormControl(voucherRawValue.deliveryDate),
      deliveryStatus: new FormControl(voucherRawValue.deliveryStatus),
    });
  }

  getVoucher(form: VoucherFormGroup): IVoucher | NewVoucher {
    return this.convertVoucherRawValueToVoucher(form.getRawValue() as VoucherFormRawValue | NewVoucherFormRawValue);
  }

  resetForm(form: VoucherFormGroup, voucher: VoucherFormGroupInput): void {
    const voucherRawValue = this.convertVoucherToVoucherRawValue({ ...this.getFormDefaults(), ...voucher });
    form.reset(
      {
        ...voucherRawValue,
        id: { value: voucherRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): VoucherFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateCreated: currentTime,
      deliveryDate: currentTime,
      deliveryStatus: false,
    };
  }

  private convertVoucherRawValueToVoucher(rawVoucher: VoucherFormRawValue | NewVoucherFormRawValue): IVoucher | NewVoucher {
    return {
      ...rawVoucher,
      dateCreated: dayjs(rawVoucher.dateCreated, DATE_TIME_FORMAT),
      deliveryDate: dayjs(rawVoucher.deliveryDate, DATE_TIME_FORMAT),
    };
  }

  private convertVoucherToVoucherRawValue(
    voucher: IVoucher | (Partial<NewVoucher> & VoucherFormDefaults),
  ): VoucherFormRawValue | PartialWithRequiredKeyOf<NewVoucherFormRawValue> {
    return {
      ...voucher,
      dateCreated: voucher.dateCreated ? voucher.dateCreated.format(DATE_TIME_FORMAT) : undefined,
      deliveryDate: voucher.deliveryDate ? voucher.deliveryDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
