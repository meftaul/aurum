import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Voucher } from 'app/shared/model/voucher.model';
import { VoucherService } from './voucher.service';
import { VoucherComponent } from './voucher.component';
import { VoucherDetailComponent } from './voucher-detail.component';
import { VoucherUpdateComponent } from './voucher-update.component';
import { VoucherDeletePopupComponent } from './voucher-delete-dialog.component';
import { IVoucher } from 'app/shared/model/voucher.model';

@Injectable({ providedIn: 'root' })
export class VoucherResolve implements Resolve<IVoucher> {
  constructor(private service: VoucherService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IVoucher> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Voucher>) => response.ok),
        map((voucher: HttpResponse<Voucher>) => voucher.body)
      );
    }
    return of(new Voucher());
  }
}

export const voucherRoute: Routes = [
  {
    path: '',
    component: VoucherComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Vouchers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: VoucherDetailComponent,
    resolve: {
      voucher: VoucherResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Vouchers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: VoucherUpdateComponent,
    resolve: {
      voucher: VoucherResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Vouchers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: VoucherUpdateComponent,
    resolve: {
      voucher: VoucherResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Vouchers'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const voucherPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: VoucherDeletePopupComponent,
    resolve: {
      voucher: VoucherResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Vouchers'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
