import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

type EntityResponseType = HttpResponse<Voucher>;

import { SERVER_API_URL } from 'app/app.constants';
import { ICustomer } from 'app/shared/model/customer.model';
import { Voucher } from 'app/shared/model/voucher.model';

@Injectable({ providedIn: 'root' })
export class TransactionService {
  public resourceUrl = SERVER_API_URL + 'api/custom-voucher/save-voucher';

  constructor(protected http: HttpClient) {}

  create(voucher: Voucher): Observable<EntityResponseType> {
    return this.http.post<ICustomer>(this.resourceUrl, voucher, { observe: 'response' });
  }
}
