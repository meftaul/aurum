import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IKarat, NewKarat } from '../karat.model';

export type PartialUpdateKarat = Partial<IKarat> & Pick<IKarat, 'id'>;

export type EntityResponseType = HttpResponse<IKarat>;
export type EntityArrayResponseType = HttpResponse<IKarat[]>;

@Injectable({ providedIn: 'root' })
export class KaratService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/karats');

  create(karat: NewKarat): Observable<EntityResponseType> {
    return this.http.post<IKarat>(this.resourceUrl, karat, { observe: 'response' });
  }

  update(karat: IKarat): Observable<EntityResponseType> {
    return this.http.put<IKarat>(`${this.resourceUrl}/${this.getKaratIdentifier(karat)}`, karat, { observe: 'response' });
  }

  partialUpdate(karat: PartialUpdateKarat): Observable<EntityResponseType> {
    return this.http.patch<IKarat>(`${this.resourceUrl}/${this.getKaratIdentifier(karat)}`, karat, { observe: 'response' });
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

  getKaratIdentifier(karat: Pick<IKarat, 'id'>): number {
    return karat.id;
  }

  compareKarat(o1: Pick<IKarat, 'id'> | null, o2: Pick<IKarat, 'id'> | null): boolean {
    return o1 && o2 ? this.getKaratIdentifier(o1) === this.getKaratIdentifier(o2) : o1 === o2;
  }

  addKaratToCollectionIfMissing<Type extends Pick<IKarat, 'id'>>(
    karatCollection: Type[],
    ...karatsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const karats: Type[] = karatsToCheck.filter(isPresent);
    if (karats.length > 0) {
      const karatCollectionIdentifiers = karatCollection.map(karatItem => this.getKaratIdentifier(karatItem));
      const karatsToAdd = karats.filter(karatItem => {
        const karatIdentifier = this.getKaratIdentifier(karatItem);
        if (karatCollectionIdentifiers.includes(karatIdentifier)) {
          return false;
        }
        karatCollectionIdentifiers.push(karatIdentifier);
        return true;
      });
      return [...karatsToAdd, ...karatCollection];
    }
    return karatCollection;
  }
}
