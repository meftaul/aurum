import { Routes } from '@angular/router';

import { Authority } from 'app/config/authority.constants';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { errorRoute } from './layouts/error/error.route';

// Account routes split between the two layouts
import activateRoute from './account/activate/activate.route';
import passwordRoute from './account/password/password.route';
import passwordResetFinishRoute from './account/password-reset/finish/password-reset-finish.route';
import passwordResetInitRoute from './account/password-reset/init/password-reset-init.route';
import registerRoute from './account/register/register.route';
import settingsRoute from './account/settings/settings.route';

// Error pages (without the catch-all wildcard, which stays at the top level)
const errorPages = errorRoute.filter(route => route.path !== '**');

const routes: Routes = [
  // ---- Public / marketing layout (top navbar) ----
  {
    path: '',
    loadComponent: () => import('./layouts/public-layout/public-layout.component'),
    children: [
      {
        path: '',
        loadComponent: () => import('./home/home.component'),
        title: 'Narayanganj Gold',
        pathMatch: 'full',
      },
      {
        path: 'login',
        loadComponent: () => import('./login/login.component'),
        title: 'login.title',
      },
      {
        path: 'account',
        children: [registerRoute, activateRoute, passwordResetInitRoute, passwordResetFinishRoute],
      },
      ...errorPages,
    ],
  },
  // ---- Logged-in layout (left sidebar + top bar) ----
  {
    path: '',
    loadComponent: () => import('./layouts/app-layout/app-layout.component'),
    children: [
      {
        path: 'dashboard',
        loadComponent: () => import('./dashboard/dashboard.component'),
        canActivate: [UserRouteAccessService],
        title: 'Dashboard',
      },
      {
        path: 'admin',
        data: {
          authorities: [Authority.ADMIN],
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./admin/admin.routes'),
      },
      {
        path: 'account',
        children: [settingsRoute, passwordRoute],
      },
      {
        path: '',
        loadChildren: () => import('./entities/entity.routes'),
      },
    ],
  },
  // ---- Catch-all (must stay last) ----
  {
    path: '**',
    redirectTo: '/404',
  },
];

export default routes;
