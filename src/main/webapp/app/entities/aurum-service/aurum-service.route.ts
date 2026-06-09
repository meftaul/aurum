import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAurumService, AurumService } from 'app/shared/model/aurum-service.model';
import { AurumServiceService } from './aurum-service.service';
import { AurumServiceComponent } from './aurum-service.component';
import { AurumServiceDetailComponent } from './aurum-service-detail.component';
import { AurumServiceUpdateComponent } from './aurum-service-update.component';

@Injectable({ providedIn: 'root' })
export class AurumServiceResolve implements Resolve<IAurumService> {
  constructor(private service: AurumServiceService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAurumService> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((aurumService: HttpResponse<AurumService>) => {
          if (aurumService.body) {
            return of(aurumService.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AurumService());
  }
}

export const aurumServiceRoute: Routes = [
  {
    path: '',
    component: AurumServiceComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'AurumServices',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AurumServiceDetailComponent,
    resolve: {
      aurumService: AurumServiceResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'AurumServices',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AurumServiceUpdateComponent,
    resolve: {
      aurumService: AurumServiceResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'AurumServices',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AurumServiceUpdateComponent,
    resolve: {
      aurumService: AurumServiceResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'AurumServices',
    },
    canActivate: [UserRouteAccessService],
  },
];
