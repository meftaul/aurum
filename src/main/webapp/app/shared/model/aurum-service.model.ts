import { IVoucher } from 'app/shared/model/voucher.model';

export interface IAurumService {
  id?: number;
  serviceType?: string;
  itemName?: string;
  quantity?: number;
  weight?: number;
  rate?: number;
  amount?: number;
  karetType?: string;
  serviceName?: string;
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
    public karetType?: string,
    public serviceName?: string,
    public voucher?: IVoucher
  ) {}
}
