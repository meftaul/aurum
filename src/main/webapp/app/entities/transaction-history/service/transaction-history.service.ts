import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITransactionHistory, NewTransactionHistory } from '../transaction-history.model';

export type PartialUpdateTransactionHistory = Partial<ITransactionHistory> & Pick<ITransactionHistory, 'id'>;

type RestOf<T extends ITransactionHistory | NewTransactionHistory> = Omit<T, 'dateCreated'> & {
  dateCreated?: string | null;
};

export type RestTransactionHistory = RestOf<ITransactionHistory>;

export type NewRestTransactionHistory = RestOf<NewTransactionHistory>;

export type PartialUpdateRestTransactionHistory = RestOf<PartialUpdateTransactionHistory>;

export type EntityResponseType = HttpResponse<ITransactionHistory>;
export type EntityArrayResponseType = HttpResponse<ITransactionHistory[]>;

@Injectable({ providedIn: 'root' })
export class TransactionHistoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/transaction-histories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(transactionHistory: NewTransactionHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transactionHistory);
    return this.http
      .post<RestTransactionHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(transactionHistory: ITransactionHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transactionHistory);
    return this.http
      .put<RestTransactionHistory>(`${this.resourceUrl}/${this.getTransactionHistoryIdentifier(transactionHistory)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(transactionHistory: PartialUpdateTransactionHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transactionHistory);
    return this.http
      .patch<RestTransactionHistory>(`${this.resourceUrl}/${this.getTransactionHistoryIdentifier(transactionHistory)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTransactionHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTransactionHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTransactionHistoryIdentifier(transactionHistory: Pick<ITransactionHistory, 'id'>): number {
    return transactionHistory.id;
  }

  compareTransactionHistory(o1: Pick<ITransactionHistory, 'id'> | null, o2: Pick<ITransactionHistory, 'id'> | null): boolean {
    return o1 && o2 ? this.getTransactionHistoryIdentifier(o1) === this.getTransactionHistoryIdentifier(o2) : o1 === o2;
  }

  addTransactionHistoryToCollectionIfMissing<Type extends Pick<ITransactionHistory, 'id'>>(
    transactionHistoryCollection: Type[],
    ...transactionHistoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const transactionHistories: Type[] = transactionHistoriesToCheck.filter(isPresent);
    if (transactionHistories.length > 0) {
      const transactionHistoryCollectionIdentifiers = transactionHistoryCollection.map(
        transactionHistoryItem => this.getTransactionHistoryIdentifier(transactionHistoryItem)!
      );
      const transactionHistoriesToAdd = transactionHistories.filter(transactionHistoryItem => {
        const transactionHistoryIdentifier = this.getTransactionHistoryIdentifier(transactionHistoryItem);
        if (transactionHistoryCollectionIdentifiers.includes(transactionHistoryIdentifier)) {
          return false;
        }
        transactionHistoryCollectionIdentifiers.push(transactionHistoryIdentifier);
        return true;
      });
      return [...transactionHistoriesToAdd, ...transactionHistoryCollection];
    }
    return transactionHistoryCollection;
  }

  protected convertDateFromClient<T extends ITransactionHistory | NewTransactionHistory | PartialUpdateTransactionHistory>(
    transactionHistory: T
  ): RestOf<T> {
    return {
      ...transactionHistory,
      dateCreated: transactionHistory.dateCreated?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restTransactionHistory: RestTransactionHistory): ITransactionHistory {
    return {
      ...restTransactionHistory,
      dateCreated: restTransactionHistory.dateCreated ? dayjs(restTransactionHistory.dateCreated) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTransactionHistory>): HttpResponse<ITransactionHistory> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTransactionHistory[]>): HttpResponse<ITransactionHistory[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
