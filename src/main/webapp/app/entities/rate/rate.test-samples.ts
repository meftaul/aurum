import { IRate, NewRate } from './rate.model';

export const sampleWithRequiredData: IRate = {
  id: 17833,
};

export const sampleWithPartialData: IRate = {
  id: 10005,
  unitPrice: 22442.25,
};

export const sampleWithFullData: IRate = {
  id: 13620,
  rateType: 'who dependency',
  unitPrice: 12275.29,
};

export const sampleWithNewData: NewRate = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
