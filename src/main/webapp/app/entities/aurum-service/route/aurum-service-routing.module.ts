import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AurumServiceComponent } from '../list/aurum-service.component';
import { AurumServiceDetailComponent } from '../detail/aurum-service-detail.component';
import { AurumServiceUpdateComponent } from '../update/aurum-service-update.component';
import { AurumServiceRoutingResolveService } from './aurum-service-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const aurumServiceRoute: Routes = [
  {
    path: '',
    component: AurumServiceComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AurumServiceDetailComponent,
    resolve: {
      aurumService: AurumServiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AurumServiceUpdateComponent,
    resolve: {
      aurumService: AurumServiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AurumServiceUpdateComponent,
    resolve: {
      aurumService: AurumServiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(aurumServiceRoute)],
  exports: [RouterModule],
})
export class AurumServiceRoutingModule {}
