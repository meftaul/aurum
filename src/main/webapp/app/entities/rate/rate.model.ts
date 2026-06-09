export interface IRate {
  id: number;
  rateType?: string | null;
  unitPrice?: number | null;
}

export type NewRate = Omit<IRate, 'id'> & { id: null };
