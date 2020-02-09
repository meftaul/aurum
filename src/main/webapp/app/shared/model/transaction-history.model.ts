import { Moment } from 'moment';
import { TransactionStatus } from 'app/shared/model/enumerations/transaction-status.model';

export interface ITransactionHistory {
  id?: number;
  voucherNo?: string;
  amount?: number;
  dateCreated?: Moment;
  tag?: TransactionStatus;
  customerId?: number;
  addedBy?: string;
}

export class TransactionHistory implements ITransactionHistory {
  constructor(
    public id?: number,
    public voucherNo?: string,
    public amount?: number,
    public dateCreated?: Moment,
    public tag?: TransactionStatus,
    public customerId?: number,
    public addedBy?: string
  ) {}
}

export class TxnReport {
  discountVoucherCount?: number;
  receivedVoucherCount?: number;
  vatVoucherCount?: number;
  refundVoucherCount?: number;

  totalDiscount?: number;
  totalReceived?: number;
  totalVat?: number;
  totalRefund?: number;
}
