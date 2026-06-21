import { Component, DestroyRef, OnInit, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

import dayjs from 'dayjs/esm';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { VoucherService } from 'app/entities/voucher/service/voucher.service';
import { IVoucher } from 'app/entities/voucher/voucher.model';
import { TransactionHistoryService } from 'app/entities/transaction-history/service/transaction-history.service';
import { ITransactionHistory } from 'app/entities/transaction-history/transaction-history.model';
import { ITxnReport } from 'app/entities/transaction-history/txn-report.model';

/** Per-widget async state so one slow/failed call never blocks the rest of the dashboard. */
interface WidgetState<T> {
  data: T;
  loading: boolean;
  error: boolean;
}

const LIST_SIZE = 5;
const ACTIVITY_SIZE = 10;

@Component({
  selector: 'jhi-user-dashboard',
  standalone: true,
  templateUrl: './user-dashboard.component.html',
  styleUrls: ['../dashboard-shared.scss', './user-dashboard.component.scss'],
  imports: [SharedModule, RouterModule, FormsModule, FormatMediumDatetimePipe],
})
export default class UserDashboardComponent implements OnInit {
  protected voucherSearch = '';

  protected readonly due = signal<WidgetState<IVoucher[]>>({ data: [], loading: true, error: false });
  protected readonly dueCount = signal(0);

  protected readonly deliveries = signal<WidgetState<IVoucher[]>>({ data: [], loading: true, error: false });
  protected readonly deliveriesCount = signal(0);

  protected readonly today = signal<WidgetState<ITxnReport | null>>({ data: null, loading: true, error: false });

  protected readonly recent = signal<WidgetState<ITransactionHistory[]>>({ data: [], loading: true, error: false });

  private readonly router = inject(Router);
  private readonly destroyRef = inject(DestroyRef);
  private readonly voucherService = inject(VoucherService);
  private readonly transactionHistoryService = inject(TransactionHistoryService);

  ngOnInit(): void {
    this.loadDueVouchers();
    this.loadPendingDeliveries();
    this.loadTodaySnapshot();
    this.loadRecentActivity();
  }

  // Jump straight to the voucher viewer, pre-loading the entered voucher number.
  searchVoucher(): void {
    const voucherNo = this.voucherSearch.trim();
    if (!voucherNo) {
      return;
    }
    this.router.navigate(['/voucher-viewer'], { queryParams: { voucherNo } });
  }

  private loadDueVouchers(): void {
    this.voucherService
      .query({ 'status.equals': 'DUE', size: LIST_SIZE, sort: ['dateCreated,desc'] })
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: res => {
          const body = res.body ?? [];
          this.dueCount.set(Number(res.headers.get(TOTAL_COUNT_RESPONSE_HEADER)) || body.length);
          this.due.set({ data: body, loading: false, error: false });
        },
        error: () => this.due.set({ data: [], loading: false, error: true }),
      });
  }

  private loadPendingDeliveries(): void {
    this.voucherService
      .query({ 'deliveryStatus.equals': false, 'status.notEquals': 'CANCEL', size: LIST_SIZE, sort: ['deliveryDate,asc'] })
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: res => {
          const body = res.body ?? [];
          this.deliveriesCount.set(Number(res.headers.get(TOTAL_COUNT_RESPONSE_HEADER)) || body.length);
          this.deliveries.set({ data: body, loading: false, error: false });
        },
        error: () => this.deliveries.set({ data: [], loading: false, error: true }),
      });
  }

  private loadTodaySnapshot(): void {
    this.transactionHistoryService
      .queryReport({ 'dateCreated.greaterThanOrEqual': dayjs().startOf('day').toISOString() })
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: res => this.today.set({ data: res.body, loading: false, error: false }),
        error: () => this.today.set({ data: null, loading: false, error: true }),
      });
  }

  private loadRecentActivity(): void {
    this.transactionHistoryService
      .query({ size: ACTIVITY_SIZE, sort: ['dateCreated,desc'] })
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: res => this.recent.set({ data: res.body ?? [], loading: false, error: false }),
        error: () => this.recent.set({ data: [], loading: false, error: true }),
      });
  }
}
