import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';

/** A single row of the "top customers by transaction volume" report (api/report/get-sale-top-n-customer). */
export interface ITopCustomer {
  customerId?: number | null;
  totalAmount?: number | null;
}

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private readonly http = inject(HttpClient);
  private readonly applicationConfigService = inject(ApplicationConfigService);

  private readonly topCustomerUrl = this.applicationConfigService.getEndpointFor('api/report/get-sale-top-n-customer');

  /**
   * Top-N customers ranked by total transaction amount within the date range.
   * Dates are passed as 'YYYY-MM-DD HH:mm:ss' strings (the backend compares them against the date_created column).
   */
  topCustomers(startDate: string, endDate: string, topN: number): Observable<ITopCustomer[]> {
    const options = createRequestOption({ startDate, endDate, topN });
    return this.http.get<ITopCustomer[]>(this.topCustomerUrl, { params: options });
  }
}
