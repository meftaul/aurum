import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IVoucher } from 'app/shared/model/voucher.model';

type EntityResponseType = HttpResponse<IVoucher>;
type EntityArrayResponseType = HttpResponse<IVoucher[]>;

@Injectable({ providedIn: 'root' })
export class VoucherService {
  public resourceUrl = SERVER_API_URL + 'api/vouchers';

  constructor(protected http: HttpClient) {}

  create(voucher: IVoucher): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(voucher);
    return this.http
      .post<IVoucher>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(voucher: IVoucher): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(voucher);
    return this.http
      .put<IVoucher>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IVoucher>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVoucher[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(voucher: IVoucher): IVoucher {
    const copy: IVoucher = Object.assign({}, voucher, {
      dateCreated: voucher.dateCreated != null && voucher.dateCreated.isValid() ? voucher.dateCreated.format(DATE_FORMAT) : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateCreated = res.body.dateCreated != null ? moment(res.body.dateCreated) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((voucher: IVoucher) => {
        voucher.dateCreated = voucher.dateCreated != null ? moment(voucher.dateCreated) : null;
      });
    }
    return res;
  }
}
