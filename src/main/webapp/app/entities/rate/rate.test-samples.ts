import { IRate, NewRate } from './rate.model';

export const sampleWithRequiredData: IRate = {
  id: 8070,
};

export const sampleWithPartialData: IRate = {
  id: 88401,
  rateType: 'Fundamental tertiary',
  unitPrice: 72747,
};

export const sampleWithFullData: IRate = {
  id: 51596,
  rateType: 'Granite',
  unitPrice: 80178,
};

export const sampleWithNewData: NewRate = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
