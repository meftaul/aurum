import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ITransactionHistory, TransactionHistory } from 'app/shared/model/transaction-history.model';
import { TransactionHistoryService } from './transaction-history.service';
import { TransactionHistoryComponent } from './transaction-history.component';
import { TransactionHistoryDetailComponent } from './transaction-history-detail.component';
import { TransactionHistoryUpdateComponent } from './transaction-history-update.component';

@Injectable({ providedIn: 'root' })
export class TransactionHistoryResolve implements Resolve<ITransactionHistory> {
  constructor(private service: TransactionHistoryService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITransactionHistory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((transactionHistory: HttpResponse<TransactionHistory>) => {
          if (transactionHistory.body) {
            return of(transactionHistory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TransactionHistory());
  }
}

export const transactionHistoryRoute: Routes = [
  {
    path: '',
    component: TransactionHistoryComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'TransactionHistories',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TransactionHistoryDetailComponent,
    resolve: {
      transactionHistory: TransactionHistoryResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'TransactionHistories',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TransactionHistoryUpdateComponent,
    resolve: {
      transactionHistory: TransactionHistoryResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'TransactionHistories',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TransactionHistoryUpdateComponent,
    resolve: {
      transactionHistory: TransactionHistoryResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'TransactionHistories',
    },
    canActivate: [UserRouteAccessService],
  },
];
