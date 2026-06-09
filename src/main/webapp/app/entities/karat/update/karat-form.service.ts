import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IKarat, NewKarat } from '../karat.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IKarat for edit and NewKaratFormGroupInput for create.
 */
type KaratFormGroupInput = IKarat | PartialWithRequiredKeyOf<NewKarat>;

type KaratFormDefaults = Pick<NewKarat, 'id'>;

type KaratFormGroupContent = {
  id: FormControl<IKarat['id'] | NewKarat['id']>;
  karatType: FormControl<IKarat['karatType']>;
  purityPercent: FormControl<IKarat['purityPercent']>;
};

export type KaratFormGroup = FormGroup<KaratFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class KaratFormService {
  createKaratFormGroup(karat: KaratFormGroupInput = { id: null }): KaratFormGroup {
    const karatRawValue = {
      ...this.getFormDefaults(),
      ...karat,
    };
    return new FormGroup<KaratFormGroupContent>({
      id: new FormControl(
        { value: karatRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      karatType: new FormControl(karatRawValue.karatType),
      purityPercent: new FormControl(karatRawValue.purityPercent, {
        validators: [Validators.min(0)],
      }),
    });
  }

  getKarat(form: KaratFormGroup): IKarat | NewKarat {
    return form.getRawValue() as IKarat | NewKarat;
  }

  resetForm(form: KaratFormGroup, karat: KaratFormGroupInput): void {
    const karatRawValue = { ...this.getFormDefaults(), ...karat };
    form.reset(
      {
        ...karatRawValue,
        id: { value: karatRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): KaratFormDefaults {
    return {
      id: null,
    };
  }
}
