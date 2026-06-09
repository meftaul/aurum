import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAurumService, NewAurumService } from '../aurum-service.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAurumService for edit and NewAurumServiceFormGroupInput for create.
 */
type AurumServiceFormGroupInput = IAurumService | PartialWithRequiredKeyOf<NewAurumService>;

type AurumServiceFormDefaults = Pick<NewAurumService, 'id'>;

type AurumServiceFormGroupContent = {
  id: FormControl<IAurumService['id'] | NewAurumService['id']>;
  serviceType: FormControl<IAurumService['serviceType']>;
  itemName: FormControl<IAurumService['itemName']>;
  quantity: FormControl<IAurumService['quantity']>;
  weight: FormControl<IAurumService['weight']>;
  rate: FormControl<IAurumService['rate']>;
  amount: FormControl<IAurumService['amount']>;
  serviceName: FormControl<IAurumService['serviceName']>;
  karatType: FormControl<IAurumService['karatType']>;
  expectedKaratType: FormControl<IAurumService['expectedKaratType']>;
  addedAlloy: FormControl<IAurumService['addedAlloy']>;
  alloyQuantity: FormControl<IAurumService['alloyQuantity']>;
  serviceCharge: FormControl<IAurumService['serviceCharge']>;
  freeCheck: FormControl<IAurumService['freeCheck']>;
  hallMarkedText: FormControl<IAurumService['hallMarkedText']>;
  weightOfFreeCheck: FormControl<IAurumService['weightOfFreeCheck']>;
  voucher: FormControl<IAurumService['voucher']>;
};

export type AurumServiceFormGroup = FormGroup<AurumServiceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AurumServiceFormService {
  createAurumServiceFormGroup(aurumService: AurumServiceFormGroupInput = { id: null }): AurumServiceFormGroup {
    const aurumServiceRawValue = {
      ...this.getFormDefaults(),
      ...aurumService,
    };
    return new FormGroup<AurumServiceFormGroupContent>({
      id: new FormControl(
        { value: aurumServiceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      serviceType: new FormControl(aurumServiceRawValue.serviceType),
      itemName: new FormControl(aurumServiceRawValue.itemName),
      quantity: new FormControl(aurumServiceRawValue.quantity, {
        validators: [Validators.min(0)],
      }),
      weight: new FormControl(aurumServiceRawValue.weight, {
        validators: [Validators.min(0)],
      }),
      rate: new FormControl(aurumServiceRawValue.rate, {
        validators: [Validators.min(0)],
      }),
      amount: new FormControl(aurumServiceRawValue.amount),
      serviceName: new FormControl(aurumServiceRawValue.serviceName),
      karatType: new FormControl(aurumServiceRawValue.karatType),
      expectedKaratType: new FormControl(aurumServiceRawValue.expectedKaratType),
      addedAlloy: new FormControl(aurumServiceRawValue.addedAlloy),
      alloyQuantity: new FormControl(aurumServiceRawValue.alloyQuantity, {
        validators: [Validators.min(0)],
      }),
      serviceCharge: new FormControl(aurumServiceRawValue.serviceCharge, {
        validators: [Validators.min(0)],
      }),
      freeCheck: new FormControl(aurumServiceRawValue.freeCheck, {
        validators: [Validators.min(0)],
      }),
      hallMarkedText: new FormControl(aurumServiceRawValue.hallMarkedText),
      weightOfFreeCheck: new FormControl(aurumServiceRawValue.weightOfFreeCheck),
      voucher: new FormControl(aurumServiceRawValue.voucher),
    });
  }

  getAurumService(form: AurumServiceFormGroup): IAurumService | NewAurumService {
    return form.getRawValue() as IAurumService | NewAurumService;
  }

  resetForm(form: AurumServiceFormGroup, aurumService: AurumServiceFormGroupInput): void {
    const aurumServiceRawValue = { ...this.getFormDefaults(), ...aurumService };
    form.reset(
      {
        ...aurumServiceRawValue,
        id: { value: aurumServiceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AurumServiceFormDefaults {
    return {
      id: null,
    };
  }
}
