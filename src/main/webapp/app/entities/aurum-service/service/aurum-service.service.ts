import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAurumService, NewAurumService } from '../aurum-service.model';

export type PartialUpdateAurumService = Partial<IAurumService> & Pick<IAurumService, 'id'>;

export type EntityResponseType = HttpResponse<IAurumService>;
export type EntityArrayResponseType = HttpResponse<IAurumService[]>;

@Injectable({ providedIn: 'root' })
export class AurumServiceService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/aurum-services');

  create(aurumService: NewAurumService): Observable<EntityResponseType> {
    return this.http.post<IAurumService>(this.resourceUrl, aurumService, { observe: 'response' });
  }

  update(aurumService: IAurumService): Observable<EntityResponseType> {
    return this.http.put<IAurumService>(`${this.resourceUrl}/${this.getAurumServiceIdentifier(aurumService)}`, aurumService, {
      observe: 'response',
    });
  }

  partialUpdate(aurumService: PartialUpdateAurumService): Observable<EntityResponseType> {
    return this.http.patch<IAurumService>(`${this.resourceUrl}/${this.getAurumServiceIdentifier(aurumService)}`, aurumService, {
      observe: 'response',
    });
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

  getAurumServiceIdentifier(aurumService: Pick<IAurumService, 'id'>): number {
    return aurumService.id;
  }

  compareAurumService(o1: Pick<IAurumService, 'id'> | null, o2: Pick<IAurumService, 'id'> | null): boolean {
    return o1 && o2 ? this.getAurumServiceIdentifier(o1) === this.getAurumServiceIdentifier(o2) : o1 === o2;
  }

  addAurumServiceToCollectionIfMissing<Type extends Pick<IAurumService, 'id'>>(
    aurumServiceCollection: Type[],
    ...aurumServicesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const aurumServices: Type[] = aurumServicesToCheck.filter(isPresent);
    if (aurumServices.length > 0) {
      const aurumServiceCollectionIdentifiers = aurumServiceCollection.map(aurumServiceItem =>
        this.getAurumServiceIdentifier(aurumServiceItem),
      );
      const aurumServicesToAdd = aurumServices.filter(aurumServiceItem => {
        const aurumServiceIdentifier = this.getAurumServiceIdentifier(aurumServiceItem);
        if (aurumServiceCollectionIdentifiers.includes(aurumServiceIdentifier)) {
          return false;
        }
        aurumServiceCollectionIdentifiers.push(aurumServiceIdentifier);
        return true;
      });
      return [...aurumServicesToAdd, ...aurumServiceCollection];
    }
    return aurumServiceCollection;
  }
}
