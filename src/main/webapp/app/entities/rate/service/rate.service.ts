import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRate, NewRate } from '../rate.model';

export type PartialUpdateRate = Partial<IRate> & Pick<IRate, 'id'>;

export type EntityResponseType = HttpResponse<IRate>;
export type EntityArrayResponseType = HttpResponse<IRate[]>;

@Injectable({ providedIn: 'root' })
export class RateService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/rates');

  create(rate: NewRate): Observable<EntityResponseType> {
    return this.http.post<IRate>(this.resourceUrl, rate, { observe: 'response' });
  }

  update(rate: IRate): Observable<EntityResponseType> {
    return this.http.put<IRate>(`${this.resourceUrl}/${this.getRateIdentifier(rate)}`, rate, { observe: 'response' });
  }

  partialUpdate(rate: PartialUpdateRate): Observable<EntityResponseType> {
    return this.http.patch<IRate>(`${this.resourceUrl}/${this.getRateIdentifier(rate)}`, rate, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRate>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRate[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRateIdentifier(rate: Pick<IRate, 'id'>): number {
    return rate.id;
  }

  compareRate(o1: Pick<IRate, 'id'> | null, o2: Pick<IRate, 'id'> | null): boolean {
    return o1 && o2 ? this.getRateIdentifier(o1) === this.getRateIdentifier(o2) : o1 === o2;
  }

  addRateToCollectionIfMissing<Type extends Pick<IRate, 'id'>>(
    rateCollection: Type[],
    ...ratesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const rates: Type[] = ratesToCheck.filter(isPresent);
    if (rates.length > 0) {
      const rateCollectionIdentifiers = rateCollection.map(rateItem => this.getRateIdentifier(rateItem));
      const ratesToAdd = rates.filter(rateItem => {
        const rateIdentifier = this.getRateIdentifier(rateItem);
        if (rateCollectionIdentifiers.includes(rateIdentifier)) {
          return false;
        }
        rateCollectionIdentifiers.push(rateIdentifier);
        return true;
      });
      return [...ratesToAdd, ...rateCollection];
    }
    return rateCollection;
  }
}
