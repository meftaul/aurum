import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAurumService } from 'app/shared/model/aurum-service.model';

type EntityResponseType = HttpResponse<IAurumService>;
type EntityArrayResponseType = HttpResponse<IAurumService[]>;

@Injectable({ providedIn: 'root' })
export class AurumServiceService {
  public resourceUrl = SERVER_API_URL + 'api/aurum-services';

  constructor(protected http: HttpClient) {}

  create(aurumService: IAurumService): Observable<EntityResponseType> {
    return this.http.post<IAurumService>(this.resourceUrl, aurumService, { observe: 'response' });
  }

  update(aurumService: IAurumService): Observable<EntityResponseType> {
    return this.http.put<IAurumService>(this.resourceUrl, aurumService, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAurumService>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAurumService[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
