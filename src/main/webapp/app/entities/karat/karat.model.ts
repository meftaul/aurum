export interface IKarat {
  id: number;
  karatType?: string | null;
  purityPercent?: number | null;
}

export type NewKarat = Omit<IKarat, 'id'> & { id: null };
