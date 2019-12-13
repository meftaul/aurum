import { Routes } from '@angular/router';

import { TransactionComponent } from './components/transaction.component';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';

export const TRANSACTION_ROUTE: Routes = [
  {
    path: '',
    component: TransactionComponent,
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_USER'],
      pageTitle: 'Transaction!'
    },
    canActivate: [UserRouteAccessService]
  }
];
