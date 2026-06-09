import dayjs from 'dayjs/esm';

import { ITransactionHistory, NewTransactionHistory } from './transaction-history.model';

export const sampleWithRequiredData: ITransactionHistory = {
  id: 9003,
  voucherNo: 'ha',
  amount: 22189.71,
  dateCreated: dayjs('2019-10-23T08:13'),
  tag: 'VAT',
  customerId: 5563,
  addedBy: 'mundane',
};

export const sampleWithPartialData: ITransactionHistory = {
  id: 14262,
  voucherNo: 'excluding',
  amount: 5369.13,
  dateCreated: dayjs('2019-10-22T14:14'),
  tag: 'DISCOUNT',
  customerId: 17567,
  addedBy: 'safely than wetly',
};

export const sampleWithFullData: ITransactionHistory = {
  id: 9343,
  voucherNo: 'abaft gah hmph',
  amount: 20558.76,
  dateCreated: dayjs('2019-10-22T09:14'),
  tag: 'REFUND',
  customerId: 10260,
  addedBy: 'wisely prickly rebound',
};

export const sampleWithNewData: NewTransactionHistory = {
  voucherNo: 'furthermore bitter',
  amount: 28372.66,
  dateCreated: dayjs('2019-10-22T09:35'),
  tag: 'VAT',
  customerId: 14955,
  addedBy: 'that',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
