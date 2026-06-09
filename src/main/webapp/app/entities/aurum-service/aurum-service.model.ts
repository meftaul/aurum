import { IVoucher } from 'app/entities/voucher/voucher.model';
import { Alloy } from 'app/entities/enumerations/alloy.model';

export interface IAurumService {
  id: number;
  serviceType?: string | null;
  itemName?: string | null;
  quantity?: number | null;
  weight?: number | null;
  rate?: number | null;
  amount?: number | null;
  serviceName?: string | null;
  karatType?: string | null;
  expectedKaratType?: string | null;
  addedAlloy?: Alloy | null;
  alloyQuantity?: number | null;
  serviceCharge?: number | null;
  freeCheck?: number | null;
  hallMarkedText?: string | null;
  weightOfFreeCheck?: string | null;
  voucher?: Pick<IVoucher, 'id' | 'voucherNo'> | null;
}

export type NewAurumService = Omit<IAurumService, 'id'> & { id: null };
