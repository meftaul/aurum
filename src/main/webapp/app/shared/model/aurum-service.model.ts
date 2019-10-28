import { IVoucher } from 'app/shared/model/voucher.model';
import { Alloy } from 'app/shared/model/enumerations/alloy.model';

export interface IAurumService {
  id?: number;
  serviceType?: string;
  itemName?: string;
  quantity?: number;
  weight?: number;
  rate?: number;
  amount?: number;
  serviceName?: string;
  karatType?: string;
  expectedKaratType?: string;
  addedAlloy?: Alloy;
  alloyQuantity?: number;
  serviceCharge?: number;
  voucher?: IVoucher;
}

export class AurumService implements IAurumService {
  constructor(
    public id?: number,
    public serviceType?: string,
    public itemName?: string,
    public quantity?: number,
    public weight?: number,
    public rate?: number,
    public amount?: number,
    public serviceName?: string,
    public karatType?: string,
    public expectedKaratType?: string,
    public addedAlloy?: Alloy,
    public alloyQuantity?: number,
    public serviceCharge?: number,
    public voucher?: IVoucher
  ) {}
}
