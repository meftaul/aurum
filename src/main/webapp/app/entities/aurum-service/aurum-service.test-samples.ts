import { IAurumService, NewAurumService } from './aurum-service.model';

export const sampleWithRequiredData: IAurumService = {
  id: 9393,
};

export const sampleWithPartialData: IAurumService = {
  id: 28688,
  serviceType: 'pixellate unlike cinder',
  itemName: 'who dowse',
  weight: 27151.61,
  rate: 2790.76,
  expectedKaratType: 'pfft stark utilization',
  alloyQuantity: 27592.72,
};

export const sampleWithFullData: IAurumService = {
  id: 7157,
  serviceType: 'glorious after',
  itemName: 'beard tame',
  quantity: 4398,
  weight: 15820.27,
  rate: 10367.12,
  amount: 22051.36,
  serviceName: 'and',
  karatType: 'underneath neatly gaseous',
  expectedKaratType: 'tensely',
  addedAlloy: 'CU',
  alloyQuantity: 31156.35,
  serviceCharge: 32214.9,
  freeCheck: 25923.17,
  hallMarkedText: 'dramatize till sandbar',
  weightOfFreeCheck: 'with wee',
};

export const sampleWithNewData: NewAurumService = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
