import dayjs from 'dayjs/esm';

import { TransactionStatus } from 'app/entities/enumerations/transaction-status.model';

import { ITransactionHistory, NewTransactionHistory } from './transaction-history.model';

export const sampleWithRequiredData: ITransactionHistory = {
  id: 37869,
  voucherNo: 'EXE Samoa Soft',
  amount: 26111,
  dateCreated: dayjs('2019-10-22T17:43'),
  tag: TransactionStatus['RECEIVE'],
  customerId: 24484,
  addedBy: 'payment Garden Trafficway',
};

export const sampleWithPartialData: ITransactionHistory = {
  id: 18572,
  voucherNo: 'Regional Mobility',
  amount: 74925,
  dateCreated: dayjs('2019-10-22T11:01'),
  tag: TransactionStatus['RECEIVE'],
  customerId: 39673,
  addedBy: 'blue',
};

export const sampleWithFullData: ITransactionHistory = {
  id: 17688,
  voucherNo: 'Licensed Vista withdrawal',
  amount: 86937,
  dateCreated: dayjs('2019-10-22T15:30'),
  tag: TransactionStatus['REFUND'],
  customerId: 3510,
  addedBy: 'Borders Concrete UIC-Franc',
};

export const sampleWithNewData: NewTransactionHistory = {
  voucherNo: 'Innovative Louisiana',
  amount: 74747,
  dateCreated: dayjs('2019-10-22T18:53'),
  tag: TransactionStatus['RECEIVE'],
  customerId: 67324,
  addedBy: 'Usability Cambridgeshire',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
