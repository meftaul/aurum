import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { ICustomer } from 'app/shared/model/customer.model';

@Injectable({ providedIn: 'root' })
export class TransactionService {
  public resourceUrl = SERVER_API_URL + 'api/customers';

  constructor(protected http: HttpClient) {}

  // for customers
  findCustomer(id: number): Observable<HttpResponse<ICustomer>> {
    return this.http.get<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
