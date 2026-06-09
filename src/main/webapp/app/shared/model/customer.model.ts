export interface ICustomer {
  id?: number;
  firstName?: string;
  lastName?: string;
  phone?: string;
  email?: string;
  address?: string;
  totalPoint?: number;
  reference?: string;
  customId?: string;
}

export class Customer implements ICustomer {
  constructor(
    public id?: number,
    public firstName?: string,
    public lastName?: string,
    public phone?: string,
    public email?: string,
    public address?: string,
    public totalPoint?: number,
    public reference?: string,
    public customId?: string
  ) {}
}
