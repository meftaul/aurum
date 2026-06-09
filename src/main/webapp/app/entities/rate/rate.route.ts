import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IRate, Rate } from 'app/shared/model/rate.model';
import { RateService } from './rate.service';
import { RateComponent } from './rate.component';
import { RateDetailComponent } from './rate-detail.component';
import { RateUpdateComponent } from './rate-update.component';

@Injectable({ providedIn: 'root' })
export class RateResolve implements Resolve<IRate> {
  constructor(private service: RateService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRate> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((rate: HttpResponse<Rate>) => {
          if (rate.body) {
            return of(rate.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Rate());
  }
}

export const rateRoute: Routes = [
  {
    path: '',
    component: RateComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'Rates',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RateDetailComponent,
    resolve: {
      rate: RateResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Rates',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RateUpdateComponent,
    resolve: {
      rate: RateResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Rates',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RateUpdateComponent,
    resolve: {
      rate: RateResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Rates',
    },
    canActivate: [UserRouteAccessService],
  },
];
