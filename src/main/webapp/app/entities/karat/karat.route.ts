import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IKarat, Karat } from 'app/shared/model/karat.model';
import { KaratService } from './karat.service';
import { KaratComponent } from './karat.component';
import { KaratDetailComponent } from './karat-detail.component';
import { KaratUpdateComponent } from './karat-update.component';

@Injectable({ providedIn: 'root' })
export class KaratResolve implements Resolve<IKarat> {
  constructor(private service: KaratService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IKarat> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((karat: HttpResponse<Karat>) => {
          if (karat.body) {
            return of(karat.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Karat());
  }
}

export const karatRoute: Routes = [
  {
    path: '',
    component: KaratComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'Karats',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: KaratDetailComponent,
    resolve: {
      karat: KaratResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Karats',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: KaratUpdateComponent,
    resolve: {
      karat: KaratResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Karats',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: KaratUpdateComponent,
    resolve: {
      karat: KaratResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Karats',
    },
    canActivate: [UserRouteAccessService],
  },
];
