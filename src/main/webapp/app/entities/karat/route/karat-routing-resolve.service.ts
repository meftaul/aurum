import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IKarat } from '../karat.model';
import { KaratService } from '../service/karat.service';

const karatResolve = (route: ActivatedRouteSnapshot): Observable<null | IKarat> => {
  const id = route.params.id;
  if (id) {
    return inject(KaratService)
      .find(id)
      .pipe(
        mergeMap((karat: HttpResponse<IKarat>) => {
          if (karat.body) {
            return of(karat.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default karatResolve;
