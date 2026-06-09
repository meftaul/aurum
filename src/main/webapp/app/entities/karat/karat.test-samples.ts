import { IKarat, NewKarat } from './karat.model';

export const sampleWithRequiredData: IKarat = {
  id: 61955,
};

export const sampleWithPartialData: IKarat = {
  id: 71113,
  purityPercent: 55670,
};

export const sampleWithFullData: IKarat = {
  id: 55170,
  karatType: 'facilitate Home',
  purityPercent: 98280,
};

export const sampleWithNewData: NewKarat = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
