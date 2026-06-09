import { IVoucher } from './voucher/voucher.model';

export class CustomVoucherDto {
  voucher: IVoucher;
  paidAmount: number;
}
