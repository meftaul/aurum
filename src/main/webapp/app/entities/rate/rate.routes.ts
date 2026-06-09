import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import RateResolve from './route/rate-routing-resolve.service';

const rateRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/rate.component').then(m => m.RateComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/rate-detail.component').then(m => m.RateDetailComponent),
    resolve: {
      rate: RateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/rate-update.component').then(m => m.RateUpdateComponent),
    resolve: {
      rate: RateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/rate-update.component').then(m => m.RateUpdateComponent),
    resolve: {
      rate: RateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default rateRoute;
