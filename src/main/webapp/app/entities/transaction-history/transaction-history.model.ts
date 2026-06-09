import dayjs from 'dayjs/esm';
import { TransactionStatus } from 'app/entities/enumerations/transaction-status.model';

export interface ITransactionHistory {
  id: number;
  voucherNo?: string | null;
  amount?: number | null;
  dateCreated?: dayjs.Dayjs | null;
  tag?: TransactionStatus | null;
  customerId?: number | null;
  addedBy?: string | null;
}

export type NewTransactionHistory = Omit<ITransactionHistory, 'id'> & { id: null };
