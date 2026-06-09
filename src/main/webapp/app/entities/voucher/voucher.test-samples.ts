import dayjs from 'dayjs/esm';

import { VoucherStatus } from 'app/entities/enumerations/voucher-status.model';

import { IVoucher, NewVoucher } from './voucher.model';

export const sampleWithRequiredData: IVoucher = {
  id: 17243,
  calculatedTotalAmount: 83049,
  status: VoucherStatus['DUE'],
  totalPayableAmount: 24651,
  addedBy: 'ivory Arkansas Incredible',
};

export const sampleWithPartialData: IVoucher = {
  id: 85630,
  customerId: 44644,
  calculatedTotalAmount: 74369,
  vat: 9835,
  disountAmount: 14445,
  status: VoucherStatus['PAID'],
  totalPayableAmount: 20510,
  addedBy: 'monitor',
  deliveryDate: dayjs('2019-10-22T23:16'),
};

export const sampleWithFullData: IVoucher = {
  id: 46159,
  voucherNo: 'efficient calculating',
  customerId: 88425,
  calculatedTotalAmount: 20311,
  vat: 54838,
  disountAmount: 63370,
  status: VoucherStatus['DUE'],
  totalPayableAmount: 16960,
  dateCreated: dayjs('2019-10-23T05:31'),
  addedBy: 'Operations',
  boxNumber: 'Mississippi Cambridgeshire asymmetric',
  deliveryDate: dayjs('2019-10-22T14:14'),
  deliveryStatus: false,
};

export const sampleWithNewData: NewVoucher = {
  calculatedTotalAmount: 5967,
  status: VoucherStatus['DUE'],
  totalPayableAmount: 39049,
  addedBy: 'Global Paradigm Cotton',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
