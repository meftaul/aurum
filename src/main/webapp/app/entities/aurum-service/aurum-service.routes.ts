import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AurumServiceResolve from './route/aurum-service-routing-resolve.service';

const aurumServiceRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/aurum-service.component').then(m => m.AurumServiceComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/aurum-service-detail.component').then(m => m.AurumServiceDetailComponent),
    resolve: {
      aurumService: AurumServiceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/aurum-service-update.component').then(m => m.AurumServiceUpdateComponent),
    resolve: {
      aurumService: AurumServiceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/aurum-service-update.component').then(m => m.AurumServiceUpdateComponent),
    resolve: {
      aurumService: AurumServiceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default aurumServiceRoute;
