import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRate } from '../rate.model';
import { RateService } from '../service/rate.service';

@Injectable({ providedIn: 'root' })
export class RateRoutingResolveService implements Resolve<IRate | null> {
  constructor(protected service: RateService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRate | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((rate: HttpResponse<IRate>) => {
          if (rate.body) {
            return of(rate.body);
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
