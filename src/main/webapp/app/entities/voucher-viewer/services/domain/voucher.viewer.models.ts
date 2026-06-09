import { IVoucher } from 'app/entities/voucher/voucher.model';
import { ITransactionHistory } from 'app/entities/transaction-history/transaction-history.model';
import { VoucherStatus } from 'app/entities/enumerations/voucher-status.model';

export class VoucherViewer {
  voucherInfo: IVoucher;
  txnHistory: ITransactionHistory[];
  totalAmount: number;
  dueAmount: number;

  constructor() {
    this.voucherInfo = ({} as IVoucher);
    this.txnHistory = [];
  }
}

export class TransactionDto {
  transactionHistory: ITransactionHistory;
  deliveryStatus: boolean;
  voucherStatus: VoucherStatus;
}
