export class Transaction {
  id: number;
  voucherNo: string;
  boxNo: string;
  karatType: string;
  deliveryDate: Date;
}

export const AURUM_SERVICE_LIST = [
  { value: 'X-Ray', viewValue: 'X-Ray' },
  { value: 'Hallmark', viewValue: 'Hallmark' },
  { value: 'Normal Melting', viewValue: 'Normal Melting' },
  { value: 'Calculated Melting', viewValue: 'Calculated Melting' }
];

export const VOUCHER_STATUS = [
  { value: 'PAID', viewValue: 'PAID' },
  { value: 'DUE', viewValue: 'DUE' },
  { value: 'CANCEL', viewValue: 'CANCEL' }
];

export const ALLOY_TYPE = [{ value: 'CU', viewValue: 'Copper' }, { value: 'SI', viewValue: 'Silver' }, { value: 'AU', viewValue: 'Gold' }];

export const SERVICE_LIST_COLUMNS = ['index', 'serviceType', 'itemName', 'karatType', 'quantity', 'weight', 'rate', 'amount', 'action'];
