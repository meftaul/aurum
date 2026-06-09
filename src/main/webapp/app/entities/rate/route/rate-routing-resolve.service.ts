import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRate } from '../rate.model';
import { RateService } from '../service/rate.service';

const rateResolve = (route: ActivatedRouteSnapshot): Observable<null | IRate> => {
  const id = route.params.id;
  if (id) {
    return inject(RateService)
      .find(id)
      .pipe(
        mergeMap((rate: HttpResponse<IRate>) => {
          if (rate.body) {
            return of(rate.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default rateResolve;
