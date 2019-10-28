export interface IKarat {
  id?: number;
  karatType?: string;
  purityPercent?: number;
}

export class Karat implements IKarat {
  constructor(public id?: number, public karatType?: string, public purityPercent?: number) {}
}
