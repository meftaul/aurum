import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITransactionHistory } from '../transaction-history.model';
import { TransactionHistoryService } from '../service/transaction-history.service';

const transactionHistoryResolve = (route: ActivatedRouteSnapshot): Observable<null | ITransactionHistory> => {
  const id = route.params.id;
  if (id) {
    return inject(TransactionHistoryService)
      .find(id)
      .pipe(
        mergeMap((transactionHistory: HttpResponse<ITransactionHistory>) => {
          if (transactionHistory.body) {
            return of(transactionHistory.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default transactionHistoryResolve;
