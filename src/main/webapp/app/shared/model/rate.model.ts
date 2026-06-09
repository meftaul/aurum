export interface IRate {
  id?: number;
  rateType?: string;
  unitPrice?: number;
}

export class Rate implements IRate {
  constructor(public id?: number, public rateType?: string, public unitPrice?: number) {}
}
