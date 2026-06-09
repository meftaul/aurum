import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

type EntityResponseType = HttpResponse<IVoucher>;

import { CustomVoucherDto } from 'app/entities/custom.voucher.model';
import { IVoucher } from 'app/entities/voucher/voucher.model';

@Injectable({ providedIn: 'root' })
export class TransactionService {
  public resourceUrl = 'api/custom-voucher/save-voucher';

  constructor(protected http: HttpClient) {}

  create(voucher: CustomVoucherDto): Observable<EntityResponseType> {
    return this.http.post<IVoucher>(this.resourceUrl, voucher, { observe: 'response' });
  }
}
