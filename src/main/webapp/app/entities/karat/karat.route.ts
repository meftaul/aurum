import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Karat } from 'app/shared/model/karat.model';
import { KaratService } from './karat.service';
import { KaratComponent } from './karat.component';
import { KaratDetailComponent } from './karat-detail.component';
import { KaratUpdateComponent } from './karat-update.component';
import { KaratDeletePopupComponent } from './karat-delete-dialog.component';
import { IKarat } from 'app/shared/model/karat.model';

@Injectable({ providedIn: 'root' })
export class KaratResolve implements Resolve<IKarat> {
  constructor(private service: KaratService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IKarat> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Karat>) => response.ok),
        map((karat: HttpResponse<Karat>) => karat.body)
      );
    }
    return of(new Karat());
  }
}

export const karatRoute: Routes = [
  {
    path: '',
    component: KaratComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Karats'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: KaratDetailComponent,
    resolve: {
      karat: KaratResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Karats'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: KaratUpdateComponent,
    resolve: {
      karat: KaratResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Karats'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: KaratUpdateComponent,
    resolve: {
      karat: KaratResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Karats'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const karatPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: KaratDeletePopupComponent,
    resolve: {
      karat: KaratResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Karats'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
