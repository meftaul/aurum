import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAurumService } from '../aurum-service.model';
import { AurumServiceService } from '../service/aurum-service.service';

@Injectable({ providedIn: 'root' })
export class AurumServiceRoutingResolveService implements Resolve<IAurumService | null> {
  constructor(protected service: AurumServiceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAurumService | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((aurumService: HttpResponse<IAurumService>) => {
          if (aurumService.body) {
            return of(aurumService.body);
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
