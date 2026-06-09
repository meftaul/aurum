import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VoucherComponent } from '../list/voucher.component';
import { VoucherDetailComponent } from '../detail/voucher-detail.component';
import { VoucherUpdateComponent } from '../update/voucher-update.component';
import { VoucherRoutingResolveService } from './voucher-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const voucherRoute: Routes = [
  {
    path: '',
    component: VoucherComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VoucherDetailComponent,
    resolve: {
      voucher: VoucherRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VoucherUpdateComponent,
    resolve: {
      voucher: VoucherRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VoucherUpdateComponent,
    resolve: {
      voucher: VoucherRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(voucherRoute)],
  exports: [RouterModule],
})
export class VoucherRoutingModule {}
