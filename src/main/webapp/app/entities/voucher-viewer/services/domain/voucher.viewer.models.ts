import { Voucher } from 'app/shared/model/voucher.model';
import { TransactionHistory } from 'app/shared/model/transaction-history.model';
import { VoucherStatus } from 'app/shared/model/enumerations/voucher-status.model';

export class VoucherViewer {
  voucherInfo: Voucher;
  txnHistory: TransactionHistory[];
  totalAmount: number;
  dueAmount: number;

  constructor() {
    this.voucherInfo = new Voucher();
    this.txnHistory = [];
  }
}

export class TransactionDto {
  transactionHistory: TransactionHistory;
  deliveryStatus: boolean;
  voucherStatus: VoucherStatus;
}
