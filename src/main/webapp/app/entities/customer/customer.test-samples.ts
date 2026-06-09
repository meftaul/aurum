import { ICustomer, NewCustomer } from './customer.model';

export const sampleWithRequiredData: ICustomer = {
  id: 24379,
  phone: '1-952-496-8402',
};

export const sampleWithPartialData: ICustomer = {
  id: 85053,
  firstName: 'Kristin',
  lastName: 'Hansen',
  phone: '(269) 931-4867',
  email: 'Dolly_Hackett65@yahoo.com',
  address: 'front-end Towels',
  totalPoint: 60686,
  reference: 'Philippines',
  customId: 'North',
};

export const sampleWithFullData: ICustomer = {
  id: 31591,
  firstName: 'Kirstin',
  lastName: 'Hintz',
  phone: '529.878.2103',
  email: 'Kaleigh76@gmail.com',
  address: 'Berkshire invoice Metal',
  totalPoint: 91626,
  reference: 'Pants throughput',
  customId: 'Awesome',
};

export const sampleWithNewData: NewCustomer = {
  phone: '796.424.5020',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
