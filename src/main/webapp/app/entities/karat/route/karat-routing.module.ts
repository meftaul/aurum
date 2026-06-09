import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { KaratComponent } from '../list/karat.component';
import { KaratDetailComponent } from '../detail/karat-detail.component';
import { KaratUpdateComponent } from '../update/karat-update.component';
import { KaratRoutingResolveService } from './karat-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const karatRoute: Routes = [
  {
    path: '',
    component: KaratComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: KaratDetailComponent,
    resolve: {
      karat: KaratRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: KaratUpdateComponent,
    resolve: {
      karat: KaratRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: KaratUpdateComponent,
    resolve: {
      karat: KaratRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(karatRoute)],
  exports: [RouterModule],
})
export class KaratRoutingModule {}
