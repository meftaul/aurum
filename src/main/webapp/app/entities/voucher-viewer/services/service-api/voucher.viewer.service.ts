import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { VoucherViewer } from '../domain/voucher.viewer.models';

type EntityResponseType = HttpResponse<VoucherViewer>;
type EntityArrayResponseType = HttpResponse<VoucherViewer[]>;

@Injectable({ providedIn: 'root' })
export class VoucherViewerService {
  public resourceUrl = SERVER_API_URL + 'api/custom-voucher';

  constructor(protected http: HttpClient) {}

  find(voucherNo: string): Observable<EntityResponseType> {
    return this.http.get<VoucherViewer>(`${this.resourceUrl}/${voucherNo}`, { observe: 'response' });
  }

  create(voucherViewer: VoucherViewer): Observable<EntityResponseType> {
    return this.http.post<VoucherViewer>(this.resourceUrl + '/save-voucher', voucherViewer, { observe: 'response' });
  }
}
