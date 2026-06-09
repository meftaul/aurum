import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IRate, NewRate } from '../rate.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRate for edit and NewRateFormGroupInput for create.
 */
type RateFormGroupInput = IRate | PartialWithRequiredKeyOf<NewRate>;

type RateFormDefaults = Pick<NewRate, 'id'>;

type RateFormGroupContent = {
  id: FormControl<IRate['id'] | NewRate['id']>;
  rateType: FormControl<IRate['rateType']>;
  unitPrice: FormControl<IRate['unitPrice']>;
};

export type RateFormGroup = FormGroup<RateFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RateFormService {
  createRateFormGroup(rate: RateFormGroupInput = { id: null }): RateFormGroup {
    const rateRawValue = {
      ...this.getFormDefaults(),
      ...rate,
    };
    return new FormGroup<RateFormGroupContent>({
      id: new FormControl(
        { value: rateRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      rateType: new FormControl(rateRawValue.rateType),
      unitPrice: new FormControl(rateRawValue.unitPrice, {
        validators: [Validators.min(0)],
      }),
    });
  }

  getRate(form: RateFormGroup): IRate | NewRate {
    return form.getRawValue() as IRate | NewRate;
  }

  resetForm(form: RateFormGroup, rate: RateFormGroupInput): void {
    const rateRawValue = { ...this.getFormDefaults(), ...rate };
    form.reset(
      {
        ...rateRawValue,
        id: { value: rateRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): RateFormDefaults {
    return {
      id: null,
    };
  }
}
