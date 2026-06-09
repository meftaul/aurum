import { IItem, NewItem } from './item.model';

export const sampleWithRequiredData: IItem = {
  id: 89800,
  name: 'Bedfordshire input',
};

export const sampleWithPartialData: IItem = {
  id: 64827,
  name: 'mobile Account',
};

export const sampleWithFullData: IItem = {
  id: 42728,
  name: 'Place Profit-focused',
  description: 'content-based next-generation Tasty',
  code: 'web-enabled Cotton',
};

export const sampleWithNewData: NewItem = {
  name: 'Malaysian methodical',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
