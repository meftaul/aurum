export interface IItem {
  id: number;
  name?: string | null;
  description?: string | null;
  code?: string | null;
}

export type NewItem = Omit<IItem, 'id'> & { id: null };
