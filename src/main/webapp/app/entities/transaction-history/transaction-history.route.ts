import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { TransactionHistory } from 'app/shared/model/transaction-history.model';
import { TransactionHistoryService } from './transaction-history.service';
import { TransactionHistoryComponent } from './transaction-history.component';
import { TransactionHistoryDetailComponent } from './transaction-history-detail.component';
import { TransactionHistoryUpdateComponent } from './transaction-history-update.component';
import { TransactionHistoryDeletePopupComponent } from './transaction-history-delete-dialog.component';
import { ITransactionHistory } from 'app/shared/model/transaction-history.model';

@Injectable({ providedIn: 'root' })
export class TransactionHistoryResolve implements Resolve<ITransactionHistory> {
  constructor(private service: TransactionHistoryService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ITransactionHistory> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<TransactionHistory>) => response.ok),
        map((transactionHistory: HttpResponse<TransactionHistory>) => transactionHistory.body)
      );
    }
    return of(new TransactionHistory());
  }
}

export const transactionHistoryRoute: Routes = [
  {
    path: '',
    component: TransactionHistoryComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Transaction Histories'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TransactionHistoryDetailComponent,
    resolve: {
      transactionHistory: TransactionHistoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Transaction Histories'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TransactionHistoryUpdateComponent,
    resolve: {
      transactionHistory: TransactionHistoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Transaction Histories'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TransactionHistoryUpdateComponent,
    resolve: {
      transactionHistory: TransactionHistoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Transaction Histories'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const transactionHistoryPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: TransactionHistoryDeletePopupComponent,
    resolve: {
      transactionHistory: TransactionHistoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Transaction Histories'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
