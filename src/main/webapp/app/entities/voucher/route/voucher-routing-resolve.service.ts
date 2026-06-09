import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVoucher } from '../voucher.model';
import { VoucherService } from '../service/voucher.service';

@Injectable({ providedIn: 'root' })
export class VoucherRoutingResolveService implements Resolve<IVoucher | null> {
  constructor(protected service: VoucherService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVoucher | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((voucher: HttpResponse<IVoucher>) => {
          if (voucher.body) {
            return of(voucher.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
