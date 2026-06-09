import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IKarat } from '../karat.model';
import { KaratService } from '../service/karat.service';

@Injectable({ providedIn: 'root' })
export class KaratRoutingResolveService implements Resolve<IKarat | null> {
  constructor(protected service: KaratService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IKarat | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((karat: HttpResponse<IKarat>) => {
          if (karat.body) {
            return of(karat.body);
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
