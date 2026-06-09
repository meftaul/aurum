import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAurumService } from '../aurum-service.model';
import { AurumServiceService } from '../service/aurum-service.service';

const aurumServiceResolve = (route: ActivatedRouteSnapshot): Observable<null | IAurumService> => {
  const id = route.params.id;
  if (id) {
    return inject(AurumServiceService)
      .find(id)
      .pipe(
        mergeMap((aurumService: HttpResponse<IAurumService>) => {
          if (aurumService.body) {
            return of(aurumService.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default aurumServiceResolve;
