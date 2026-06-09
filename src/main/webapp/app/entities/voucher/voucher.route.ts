import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IVoucher, Voucher } from 'app/shared/model/voucher.model';
import { VoucherService } from './voucher.service';
import { VoucherComponent } from './voucher.component';
import { VoucherDetailComponent } from './voucher-detail.component';
import { VoucherUpdateComponent } from './voucher-update.component';

@Injectable({ providedIn: 'root' })
export class VoucherResolve implements Resolve<IVoucher> {
  constructor(private service: VoucherService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVoucher> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((voucher: HttpResponse<Voucher>) => {
          if (voucher.body) {
            return of(voucher.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Voucher());
  }
}

export const voucherRoute: Routes = [
  {
    path: '',
    component: VoucherComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'Vouchers',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VoucherDetailComponent,
    resolve: {
      voucher: VoucherResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Vouchers',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VoucherUpdateComponent,
    resolve: {
      voucher: VoucherResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Vouchers',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VoucherUpdateComponent,
    resolve: {
      voucher: VoucherResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Vouchers',
    },
    canActivate: [UserRouteAccessService],
  },
];
