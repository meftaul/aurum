import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { AurumService } from 'app/shared/model/aurum-service.model';
import { AurumServiceService } from './aurum-service.service';
import { AurumServiceComponent } from './aurum-service.component';
import { AurumServiceDetailComponent } from './aurum-service-detail.component';
import { AurumServiceUpdateComponent } from './aurum-service-update.component';
import { AurumServiceDeletePopupComponent } from './aurum-service-delete-dialog.component';
import { IAurumService } from 'app/shared/model/aurum-service.model';

@Injectable({ providedIn: 'root' })
export class AurumServiceResolve implements Resolve<IAurumService> {
  constructor(private service: AurumServiceService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IAurumService> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<AurumService>) => response.ok),
        map((aurumService: HttpResponse<AurumService>) => aurumService.body)
      );
    }
    return of(new AurumService());
  }
}

export const aurumServiceRoute: Routes = [
  {
    path: '',
    component: AurumServiceComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'AurumServices'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: AurumServiceDetailComponent,
    resolve: {
      aurumService: AurumServiceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'AurumServices'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: AurumServiceUpdateComponent,
    resolve: {
      aurumService: AurumServiceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'AurumServices'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: AurumServiceUpdateComponent,
    resolve: {
      aurumService: AurumServiceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'AurumServices'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const aurumServicePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: AurumServiceDeletePopupComponent,
    resolve: {
      aurumService: AurumServiceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'AurumServices'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
