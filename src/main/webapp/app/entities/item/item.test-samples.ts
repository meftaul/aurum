import { IItem, NewItem } from './item.model';

export const sampleWithRequiredData: IItem = {
  id: 27990,
  name: 'yippee',
};

export const sampleWithPartialData: IItem = {
  id: 21197,
  name: 'yahoo decouple curiously',
  description: 'elegant',
  code: 'ick pearl',
};

export const sampleWithFullData: IItem = {
  id: 23534,
  name: 'evince',
  description: 'um grumpy cod',
  code: 'bewail dependency readily',
};

export const sampleWithNewData: NewItem = {
  name: 'considering whose',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
