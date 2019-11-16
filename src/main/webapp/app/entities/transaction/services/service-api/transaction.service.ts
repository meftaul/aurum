import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

type EntityResponseType = HttpResponse<CustomVoucherDto>;

import { SERVER_API_URL } from 'app/app.constants';
import { CustomVoucherDto } from 'app/shared/model/custom.voucher.model';

@Injectable({ providedIn: 'root' })
export class TransactionService {
  public resourceUrl = SERVER_API_URL + 'api/custom-voucher/save-voucher';

  constructor(protected http: HttpClient) {}

  create(voucher: CustomVoucherDto): Observable<EntityResponseType> {
    return this.http.post<CustomVoucherDto>(this.resourceUrl, voucher, { observe: 'response' });
  }
}
