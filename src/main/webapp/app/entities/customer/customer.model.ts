export interface ICustomer {
  id: number;
  firstName?: string | null;
  lastName?: string | null;
  phone?: string | null;
  email?: string | null;
  address?: string | null;
  totalPoint?: number | null;
  reference?: string | null;
  customId?: string | null;
}

export type NewCustomer = Omit<ICustomer, 'id'> & { id: null };
