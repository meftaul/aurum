import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { ITransactionHistory, TxnReport } from 'app/shared/model/transaction-history.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { TransactionHistoryService } from './transaction-history.service';

interface Tag {
  value: string;
  viewValue: string;
}

@Component({
  selector: 'jhi-transaction-history',
  templateUrl: './transaction-history.component.html',
  styleUrls: ['transaction-history.component.scss']
})
export class TransactionHistoryComponent implements OnInit, OnDestroy {
  tags: Tag[] = [
    { value: 'RECEIVE', viewValue: 'RECEIVE' },
    { value: 'REFUND', viewValue: 'REFUND' },
    { value: 'DISCOUNT', viewValue: 'DISCOUNT' },
    { value: 'VAT', viewValue: 'VAT' }
  ];

  currentAccount: any;
  transactionHistories: ITransactionHistory[];
  error: any;
  success: any;
  eventSubscriber: Subscription;
  routeData: any;
  links: any;
  totalItems: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  previousPage: any;
  reverse: any;

  voucherNo: string;
  startDate: Date;
  endDate: Date;
  tag: string;
  txnReport: any;

  constructor(
    protected transactionHistoryService: TransactionHistoryService,
    protected parseLinks: JhiParseLinks,
    protected jhiAlertService: JhiAlertService,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.routeData = this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.previousPage = data.pagingParams.page;
      this.reverse = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
    });
  }

  loadAll() {
    const req = {
      page: this.page - 1,
      size: this.itemsPerPage,
      sort: this.sort()
    };

    if (this.startDate != null) {
      const startIsoDate = new Date(this.startDate.getTime() - this.startDate.getTimezoneOffset() * 60000).toISOString();
      req['dateCreated.greaterThanOrEqual'] = startIsoDate;
    }

    if (this.endDate != null) {
      const endIsoDate = new Date(this.endDate.getTime() - this.endDate.getTimezoneOffset() * 60000).toISOString();
      req['dateCreated.lessThanOrEqual'] = endIsoDate;
    }

    if (this.tag != null) {
      req['tag.equals'] = this.tag;
    }

    if (this.voucherNo != null) {
      req['voucherNo.equals'] = this.voucherNo;
    }

    this.transactionHistoryService
      .query(req)
      .subscribe(
        (res: HttpResponse<ITransactionHistory[]>) => this.paginateTransactionHistories(res.body, res.headers),
        (res: HttpErrorResponse) => this.onError(res.message)
      );

    this.transactionHistoryService.queryReport(req).subscribe((data: HttpResponse<any>) => {
      this.txnReport = data.body;
    });
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/transaction-history'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    });
    this.loadAll();
  }

  resetFilter() {
    this.voucherNo = null;
    this.startDate = null;
    this.endDate = null;
    this.tag = null;
    this.loadAll();
  }

  clear() {
    this.page = 0;
    this.router.navigate([
      '/transaction-history',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInTransactionHistories();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ITransactionHistory) {
    return item.id;
  }

  registerChangeInTransactionHistories() {
    this.eventSubscriber = this.eventManager.subscribe('transactionHistoryListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateTransactionHistories(data: ITransactionHistory[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.transactionHistories = data;
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
