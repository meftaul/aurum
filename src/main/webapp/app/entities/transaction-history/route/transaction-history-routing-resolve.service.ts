import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITransactionHistory } from '../transaction-history.model';
import { TransactionHistoryService } from '../service/transaction-history.service';

@Injectable({ providedIn: 'root' })
export class TransactionHistoryRoutingResolveService implements Resolve<ITransactionHistory | null> {
  constructor(protected service: TransactionHistoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITransactionHistory | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((transactionHistory: HttpResponse<ITransactionHistory>) => {
          if (transactionHistory.body) {
            return of(transactionHistory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
