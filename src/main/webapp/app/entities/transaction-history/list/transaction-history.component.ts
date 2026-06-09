import { Component, NgZone, OnInit, inject, signal } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { Observable, Subscription, combineLatest, filter, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import dayjs from 'dayjs/esm';

import SharedModule from 'app/shared/shared.module';
import HasAnyAuthorityDirective from 'app/shared/auth/has-any-authority.directive';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { ItemCountComponent } from 'app/shared/pagination';
import { FormsModule } from '@angular/forms';
import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { FilterOptions, IFilterOption, IFilterOptions } from 'app/shared/filter';
import { ITransactionHistory } from '../transaction-history.model';

import { EntityArrayResponseType, TransactionHistoryService } from '../service/transaction-history.service';
import { TransactionHistoryDeleteDialogComponent } from '../delete/transaction-history-delete-dialog.component';

@Component({
  selector: 'jhi-transaction-history',
  templateUrl: './transaction-history.component.html',
  styleUrls: ['../transaction-history.component.scss'],
  imports: [
    RouterModule,
    FormsModule,
    SharedModule,
    HasAnyAuthorityDirective,
    SortDirective,
    SortByDirective,
    FormatMediumDatetimePipe,
    ItemCountComponent,
  ],
})
export class TransactionHistoryComponent implements OnInit {
  subscription: Subscription | null = null;
  transactionHistories = signal<ITransactionHistory[]>([]);
  isLoading = false;

  sortState = sortStateSignal({});
  filters: IFilterOptions = new FilterOptions();

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  // Custom reporting dashboard (ported from the JHipster 6 app). Drives both the
  // aggregated report cards (GET api/transaction-report) and the table below.
  tags = [
    { value: 'RECEIVE', viewValue: 'RECEIVE' },
    { value: 'REFUND', viewValue: 'REFUND' },
    { value: 'DISCOUNT', viewValue: 'DISCOUNT' },
    { value: 'VAT', viewValue: 'VAT' },
  ];
  voucherNo = '';
  startDate = dayjs().format('YYYY-MM-DD');
  endDate = dayjs().format('YYYY-MM-DD');
  tag = 'RECEIVE';
  title = "Today's Report";
  txnReport: any = null;

  public readonly router = inject(Router);
  protected readonly transactionHistoryService = inject(TransactionHistoryService);
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);

  trackId = (item: ITransactionHistory): number => this.transactionHistoryService.getTransactionHistoryIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();

    this.filters.filterChanges.subscribe(filterOptions => this.handleNavigation(1, this.sortState(), filterOptions));

    this.loadReport();
  }

  /** Re-run both the table query and the aggregated report cards for the current filter values. */
  search(): void {
    this.title = 'Custom Report';
    this.page = 1;
    this.load();
    this.loadReport();
  }

  resetFilter(): void {
    this.voucherNo = '';
    this.startDate = '';
    this.endDate = '';
    this.tag = '';
    this.title = 'All';
    this.page = 1;
    this.load();
    this.loadReport();
  }

  /** Quick presets: today / current week / current month, scoped to RECEIVE transactions. */
  updateDate(type: string): void {
    this.tag = 'RECEIVE';
    const today = dayjs();
    if (type === 'daily') {
      this.startDate = today.format('YYYY-MM-DD');
      this.endDate = today.format('YYYY-MM-DD');
      this.title = "Today's Report";
    } else if (type === 'week') {
      this.endDate = today.format('YYYY-MM-DD');
      this.startDate = today.startOf('week').format('YYYY-MM-DD');
      this.title = 'Weekly Report';
    } else if (type === 'month') {
      this.endDate = today.format('YYYY-MM-DD');
      this.startDate = today.startOf('month').format('YYYY-MM-DD');
      this.title = 'Monthly Report';
    }
    this.page = 1;
    this.load();
    this.loadReport();
  }

  delete(transactionHistory: ITransactionHistory): void {
    const modalRef = this.modalService.open(TransactionHistoryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.transactionHistory = transactionHistory;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  load(): void {
    this.queryBackend().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(this.page, event, this.filters.filterOptions);
  }

  navigateToPage(page: number): void {
    this.handleNavigation(page, this.sortState(), this.filters.filterOptions);
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page = +(page ?? 1);
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
    this.filters.initializeFromParams(params);
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.transactionHistories.set(dataFromBody);
  }

  protected fillComponentAttributesFromResponseBody(data: ITransactionHistory[] | null): ITransactionHistory[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected queryBackend(): Observable<EntityArrayResponseType> {
    const { page, filters } = this;

    this.isLoading = true;
    const pageToLoad: number = page;
    const queryObject: any = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    filters.filterOptions.forEach(filterOption => {
      queryObject[filterOption.name] = filterOption.values;
    });
    Object.assign(queryObject, this.buildReportFilter());
    return this.transactionHistoryService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected loadReport(): void {
    this.transactionHistoryService.queryReport(this.buildReportFilter()).subscribe({
      next: res => {
        this.txnReport = res.body;
      },
    });
  }

  /** Builds the TransactionHistoryCriteria query params shared by the table and the report endpoint. */
  protected buildReportFilter(): any {
    const req: any = {};
    if (this.startDate) {
      req['dateCreated.greaterThanOrEqual'] = dayjs(this.startDate).startOf('day').toISOString();
    }
    if (this.endDate) {
      req['dateCreated.lessThanOrEqual'] = dayjs(this.endDate).endOf('day').toISOString();
    }
    if (this.tag) {
      req['tag.equals'] = this.tag;
    }
    if (this.voucherNo) {
      req['voucherNo.contains'] = this.voucherNo;
    }
    return req;
  }

  protected handleNavigation(page: number, sortState: SortState, filterOptions?: IFilterOption[]): void {
    const queryParamsObj: any = {
      page,
      size: this.itemsPerPage,
      sort: this.sortService.buildSortParam(sortState),
    };

    filterOptions?.forEach(filterOption => {
      queryParamsObj[filterOption.nameAsQueryParam()] = filterOption.values;
    });

    this.ngZone.run(() => {
      this.router.navigate(['./'], {
        relativeTo: this.activatedRoute,
        queryParams: queryParamsObj,
      });
    });
  }
}
