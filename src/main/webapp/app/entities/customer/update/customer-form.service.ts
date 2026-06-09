import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICustomer, NewCustomer } from '../customer.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICustomer for edit and NewCustomerFormGroupInput for create.
 */
type CustomerFormGroupInput = ICustomer | PartialWithRequiredKeyOf<NewCustomer>;

type CustomerFormDefaults = Pick<NewCustomer, 'id'>;

type CustomerFormGroupContent = {
  id: FormControl<ICustomer['id'] | NewCustomer['id']>;
  firstName: FormControl<ICustomer['firstName']>;
  lastName: FormControl<ICustomer['lastName']>;
  phone: FormControl<ICustomer['phone']>;
  email: FormControl<ICustomer['email']>;
  address: FormControl<ICustomer['address']>;
  totalPoint: FormControl<ICustomer['totalPoint']>;
  reference: FormControl<ICustomer['reference']>;
  customId: FormControl<ICustomer['customId']>;
};

export type CustomerFormGroup = FormGroup<CustomerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CustomerFormService {
  createCustomerFormGroup(customer: CustomerFormGroupInput = { id: null }): CustomerFormGroup {
    const customerRawValue = {
      ...this.getFormDefaults(),
      ...customer,
    };
    return new FormGroup<CustomerFormGroupContent>({
      id: new FormControl(
        { value: customerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      firstName: new FormControl(customerRawValue.firstName),
      lastName: new FormControl(customerRawValue.lastName),
      phone: new FormControl(customerRawValue.phone, {
        validators: [Validators.required],
      }),
      email: new FormControl(customerRawValue.email),
      address: new FormControl(customerRawValue.address),
      totalPoint: new FormControl(customerRawValue.totalPoint, {
        validators: [Validators.min(0)],
      }),
      reference: new FormControl(customerRawValue.reference),
      customId: new FormControl(customerRawValue.customId),
    });
  }

  getCustomer(form: CustomerFormGroup): ICustomer | NewCustomer {
    return form.getRawValue() as ICustomer | NewCustomer;
  }

  resetForm(form: CustomerFormGroup, customer: CustomerFormGroupInput): void {
    const customerRawValue = { ...this.getFormDefaults(), ...customer };
    form.reset(
      {
        ...customerRawValue,
        id: { value: customerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CustomerFormDefaults {
    return {
      id: null,
    };
  }
}
