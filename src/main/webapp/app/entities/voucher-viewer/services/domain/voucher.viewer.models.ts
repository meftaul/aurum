import { Voucher } from 'app/shared/model/voucher.model';
import { TransactionHistory } from 'app/shared/model/transaction-history.model';

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
