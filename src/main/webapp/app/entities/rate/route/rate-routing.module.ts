import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RateComponent } from '../list/rate.component';
import { RateDetailComponent } from '../detail/rate-detail.component';
import { RateUpdateComponent } from '../update/rate-update.component';
import { RateRoutingResolveService } from './rate-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const rateRoute: Routes = [
  {
    path: '',
    component: RateComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RateDetailComponent,
    resolve: {
      rate: RateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RateUpdateComponent,
    resolve: {
      rate: RateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RateUpdateComponent,
    resolve: {
      rate: RateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(rateRoute)],
  exports: [RouterModule],
})
export class RateRoutingModule {}
