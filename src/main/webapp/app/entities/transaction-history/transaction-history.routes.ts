import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TransactionHistoryResolve from './route/transaction-history-routing-resolve.service';

const transactionHistoryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/transaction-history.component').then(m => m.TransactionHistoryComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/transaction-history-detail.component').then(m => m.TransactionHistoryDetailComponent),
    resolve: {
      transactionHistory: TransactionHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/transaction-history-update.component').then(m => m.TransactionHistoryUpdateComponent),
    resolve: {
      transactionHistory: TransactionHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/transaction-history-update.component').then(m => m.TransactionHistoryUpdateComponent),
    resolve: {
      transactionHistory: TransactionHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default transactionHistoryRoute;
