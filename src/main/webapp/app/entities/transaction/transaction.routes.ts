import { Route } from '@angular/router';

import { TransactionComponent } from './components/transaction.component';

export const TRANSACTION_ROUTE: Route = {
  path: '',
  component: TransactionComponent,
  data: {
    authorities: [],
    pageTitle: 'Aurum: Transaction!'
  }
};
