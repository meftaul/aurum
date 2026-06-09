import { Alloy } from 'app/entities/enumerations/alloy.model';

import { IAurumService, NewAurumService } from './aurum-service.model';

export const sampleWithRequiredData: IAurumService = {
  id: 66636,
};

export const sampleWithPartialData: IAurumService = {
  id: 3488,
  weight: 65412,
  rate: 62801,
  serviceName: 'front-end payment',
  freeCheck: 48532,
  hallMarkedText: 'system Producer',
  weightOfFreeCheck: 'Sleek',
};

export const sampleWithFullData: IAurumService = {
  id: 73392,
  serviceType: 'Sausages Customer',
  itemName: 'Synchronised Concrete',
  quantity: 8701,
  weight: 53756,
  rate: 9521,
  amount: 35616,
  serviceName: 'payment Avon',
  karatType: 'Executive Account',
  expectedKaratType: 'e-business',
  addedAlloy: Alloy['SI'],
  alloyQuantity: 84713,
  serviceCharge: 32942,
  freeCheck: 3307,
  hallMarkedText: 'Credit cross-platform Forward',
  weightOfFreeCheck: 'Personal COM dot-com',
};

export const sampleWithNewData: NewAurumService = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
