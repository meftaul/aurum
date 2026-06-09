import dayjs from 'dayjs/esm';
import { VoucherStatus } from 'app/entities/enumerations/voucher-status.model';

export interface IVoucher {
  id: number;
  voucherNo?: string | null;
  customerId?: number | null;
  calculatedTotalAmount?: number | null;
  vat?: number | null;
  disountAmount?: number | null;
  status?: keyof typeof VoucherStatus | null;
  totalPayableAmount?: number | null;
  dateCreated?: dayjs.Dayjs | null;
  addedBy?: string | null;
  boxNumber?: string | null;
  deliveryDate?: dayjs.Dayjs | null;
  deliveryStatus?: boolean | null;
}

export type NewVoucher = Omit<IVoucher, 'id'> & { id: null };
