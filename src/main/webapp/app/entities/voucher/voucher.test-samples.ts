import dayjs from 'dayjs/esm';

import { IVoucher, NewVoucher } from './voucher.model';

export const sampleWithRequiredData: IVoucher = {
  id: 26517,
  calculatedTotalAmount: 29302.72,
  status: 'DUE',
  totalPayableAmount: 6459.23,
  addedBy: 'tromp fictionalize',
};

export const sampleWithPartialData: IVoucher = {
  id: 14669,
  voucherNo: 'during gah ugh',
  calculatedTotalAmount: 15905.67,
  status: 'PAID',
  totalPayableAmount: 28113.62,
  dateCreated: dayjs('2019-10-22T16:00'),
  addedBy: 'deafening',
  boxNumber: 'until',
  deliveryStatus: false,
};

export const sampleWithFullData: IVoucher = {
  id: 19024,
  voucherNo: 'promise dress',
  customerId: 13252,
  calculatedTotalAmount: 27443.92,
  vat: 26404.65,
  disountAmount: 4420.58,
  status: 'PAID',
  totalPayableAmount: 12308.86,
  dateCreated: dayjs('2019-10-23T05:00'),
  addedBy: 'subtle upset euphonium',
  boxNumber: 'yum octave doodle',
  deliveryDate: dayjs('2019-10-23T08:16'),
  deliveryStatus: true,
};

export const sampleWithNewData: NewVoucher = {
  calculatedTotalAmount: 1474.65,
  status: 'PAID',
  totalPayableAmount: 25366.68,
  addedBy: 'huzzah',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
