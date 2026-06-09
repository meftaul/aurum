import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IKarat } from 'app/shared/model/karat.model';

type EntityResponseType = HttpResponse<IKarat>;
type EntityArrayResponseType = HttpResponse<IKarat[]>;

@Injectable({ providedIn: 'root' })
export class KaratService {
  public resourceUrl = SERVER_API_URL + 'api/karats';

  constructor(protected http: HttpClient) {}

  create(karat: IKarat): Observable<EntityResponseType> {
    return this.http.post<IKarat>(this.resourceUrl, karat, { observe: 'response' });
  }

  update(karat: IKarat): Observable<EntityResponseType> {
    return this.http.put<IKarat>(this.resourceUrl, karat, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IKarat>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IKarat[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
