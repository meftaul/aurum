import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { createRequestOption } from 'app/shared/util/request-util';
import { Report } from 'app/entities/report/domain/report.model';
import { map } from 'rxjs/operators';

type EntityResponseType = HttpResponse<Report>;
type EntityArrayResponseType = HttpResponse<Report[]>;

@Injectable({ providedIn: 'root' })
export class ReportService {
  public resourceUrl = SERVER_API_URL + 'api/report/get-report';

  constructor(protected http: HttpClient) {}

  getReport(req: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<Report[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => res));
  }
}
