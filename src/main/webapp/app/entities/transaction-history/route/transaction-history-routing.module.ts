import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TransactionHistoryComponent } from '../list/transaction-history.component';
import { TransactionHistoryDetailComponent } from '../detail/transaction-history-detail.component';
import { TransactionHistoryUpdateComponent } from '../update/transaction-history-update.component';
import { TransactionHistoryRoutingResolveService } from './transaction-history-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const transactionHistoryRoute: Routes = [
  {
    path: '',
    component: TransactionHistoryComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TransactionHistoryDetailComponent,
    resolve: {
      transactionHistory: TransactionHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TransactionHistoryUpdateComponent,
    resolve: {
      transactionHistory: TransactionHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TransactionHistoryUpdateComponent,
    resolve: {
      transactionHistory: TransactionHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(transactionHistoryRoute)],
  exports: [RouterModule],
})
export class TransactionHistoryRoutingModule {}
