import { Routes } from '@angular/router';

import { TransactionComponent } from './components/transaction.component';
import { CustomerFormPopupComponent } from './components/customer-form/customer.form.component';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';

export const TRANSACTION_ROUTE: Routes = [
  {
    path: '',
    component: TransactionComponent,
    data: {
      authorities: [],
      pageTitle: 'Aurum: Transaction!'
    }
  },
  {
    path: '/create-customer',
    component: CustomerFormPopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Customers'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
