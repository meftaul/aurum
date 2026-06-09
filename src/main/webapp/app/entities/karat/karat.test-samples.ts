import { IKarat, NewKarat } from './karat.model';

export const sampleWithRequiredData: IKarat = {
  id: 21662,
};

export const sampleWithPartialData: IKarat = {
  id: 28249,
  karatType: 'reasonable blindly sleepily',
  purityPercent: 9114.9,
};

export const sampleWithFullData: IKarat = {
  id: 18326,
  karatType: 'safely fast through',
  purityPercent: 10164.42,
};

export const sampleWithNewData: NewKarat = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
