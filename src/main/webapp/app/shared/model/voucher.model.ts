import { Moment } from 'moment';
import { VoucherStatus } from 'app/shared/model/enumerations/voucher-status.model';

export interface IVoucher {
  id?: number;
  voucherNo?: string;
  customerId?: number;
  calculatedTotalAmount?: number;
  vat?: number;
  disountAmount?: number;
  status?: VoucherStatus;
  totalPayableAmount?: number;
  dateCreated?: Moment;
  addedBy?: string;
}

export class Voucher implements IVoucher {
  constructor(
    public id?: number,
    public voucherNo?: string,
    public customerId?: number,
    public calculatedTotalAmount?: number,
    public vat?: number,
    public disountAmount?: number,
    public status?: VoucherStatus,
    public totalPayableAmount?: number,
    public dateCreated?: Moment,
    public addedBy?: string
  ) {}
}
