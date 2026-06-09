import { ICustomer, NewCustomer } from './customer.model';

export const sampleWithRequiredData: ICustomer = {
  id: 3366,
  phone: '(752) 629-8657 x4628',
};

export const sampleWithPartialData: ICustomer = {
  id: 30079,
  phone: '1-641-443-5194 x6939',
  totalPoint: 24554,
  reference: 'smoggy yearly deliberately',
};

export const sampleWithFullData: ICustomer = {
  id: 4149,
  firstName: 'Lyla',
  lastName: 'Langosh',
  phone: '306.812.3787 x40679',
  email: 'Pearl.Mosciski36@gmail.com',
  address: 'instead',
  totalPoint: 10695,
  reference: 'next',
  customId: 'affect bolster',
};

export const sampleWithNewData: NewCustomer = {
  phone: '815.843.9799 x216',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
