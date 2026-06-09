import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { VoucherViewer, TransactionDto } from '../domain/voucher.viewer.models';
import { ITransactionHistory } from 'app/entities/transaction-history/transaction-history.model';

type EntityResponseType = HttpResponse<VoucherViewer>;
type EntityArrayResponseType = HttpResponse<VoucherViewer[]>;

@Injectable({ providedIn: 'root' })
export class VoucherViewerService {
  public resourceUrl = 'api/custom-voucher';

  constructor(protected http: HttpClient) {}

  find(voucherNo: string): Observable<EntityResponseType> {
    return this.http.get<VoucherViewer>(`${this.resourceUrl}/${voucherNo}`, { observe: 'response' });
  }

  create(voucherViewer: VoucherViewer): Observable<EntityResponseType> {
    return this.http.post<VoucherViewer>(this.resourceUrl + '/save-voucher', voucherViewer, { observe: 'response' });
  }

  createCustomeTransaction(transactionDto: TransactionDto): Observable<HttpResponse<ITransactionHistory>> {
    return this.http.post<ITransactionHistory>(this.resourceUrl + '/custom-transaction', transactionDto, { observe: 'response' });
  }

  deleteVoucher(voucherNo: string): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/delete/${voucherNo}`, { observe: 'response' });
  }
}
