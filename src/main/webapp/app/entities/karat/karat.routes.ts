import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import KaratResolve from './route/karat-routing-resolve.service';

const karatRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/karat.component').then(m => m.KaratComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/karat-detail.component').then(m => m.KaratDetailComponent),
    resolve: {
      karat: KaratResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/karat-update.component').then(m => m.KaratUpdateComponent),
    resolve: {
      karat: KaratResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/karat-update.component').then(m => m.KaratUpdateComponent),
    resolve: {
      karat: KaratResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default karatRoute;
