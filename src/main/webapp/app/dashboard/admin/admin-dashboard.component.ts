import { Component, DestroyRef, OnInit, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormsModule } from '@angular/forms';
import { forkJoin, of } from 'rxjs';
import { catchError, map, switchMap } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { NgxChartsModule } from '@swimlane/ngx-charts';

import SharedModule from 'app/shared/shared.module';
import { TransactionHistoryService } from 'app/entities/transaction-history/service/transaction-history.service';
import { ITxnReport } from 'app/entities/transaction-history/txn-report.model';
import { ReportService } from 'app/entities/report/service-api/report.service';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { ICustomer } from 'app/entities/customer/customer.model';
import { DashboardService } from '../service/dashboard.service';

interface WidgetState<T> {
  data: T;
  loading: boolean;
  error: boolean;
}

/** {name, value} pair consumed by ngx-charts pie/bar charts. */
interface ChartDatum {
  name: string;
  value: number;
}

type Period = 'today' | 'week' | 'month' | 'custom';

const TOP_N = 5;
const TREND_DAYS = 30;
const BACKEND_DATE_FORMAT = 'YYYY-MM-DD HH:mm:ss';

@Component({
  selector: 'jhi-admin-dashboard',
  standalone: true,
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['../dashboard-shared.scss', './admin-dashboard.component.scss'],
  imports: [SharedModule, FormsModule, NgxChartsModule],
})
export default class AdminDashboardComponent implements OnInit {
  protected readonly period = signal<Period>('month');
  protected customStart = '';
  protected customEnd = '';

  protected readonly report = signal<WidgetState<ITxnReport | null>>({ data: null, loading: true, error: false });
  protected readonly breakdown = signal<ChartDatum[]>([]);
  protected readonly trend = signal<WidgetState<{ name: string; series: ChartDatum[] }[]>>({ data: [], loading: true, error: false });
  protected readonly topCustomers = signal<WidgetState<ChartDatum[]>>({ data: [], loading: true, error: false });

  /** Green + gold palette aligned with the app theme. */
  protected readonly colorScheme = { domain: ['#C9A227', '#1F7A4D', '#8B6F1E', '#2E9E63', '#D9C26B', '#145C39'] };

  private readonly destroyRef = inject(DestroyRef);
  private readonly transactionHistoryService = inject(TransactionHistoryService);
  private readonly reportService = inject(ReportService);
  private readonly customerService = inject(CustomerService);
  private readonly dashboardService = inject(DashboardService);

  ngOnInit(): void {
    this.loadPeriodWidgets();
    this.loadTrend();
  }

  protected selectPeriod(period: Period): void {
    this.period.set(period);
    if (period !== 'custom') {
      this.loadPeriodWidgets();
    }
  }

  protected applyCustomRange(): void {
    if (this.customStart && this.customEnd) {
      this.loadPeriodWidgets();
    }
  }

  private currentRange(): { start: dayjs.Dayjs; end: dayjs.Dayjs } {
    switch (this.period()) {
      case 'today':
        return { start: dayjs().startOf('day'), end: dayjs().endOf('day') };
      case 'week':
        return { start: dayjs().startOf('week'), end: dayjs().endOf('week') };
      case 'custom':
        return {
          start: this.customStart ? dayjs(this.customStart).startOf('day') : dayjs().startOf('month'),
          end: this.customEnd ? dayjs(this.customEnd).endOf('day') : dayjs().endOf('day'),
        };
      case 'month':
      default:
        return { start: dayjs().startOf('month'), end: dayjs().endOf('month') };
    }
  }

  private loadPeriodWidgets(): void {
    const { start, end } = this.currentRange();
    this.loadKpis(start, end);
    this.loadTopCustomers(start, end);
  }

  private loadKpis(start: dayjs.Dayjs, end: dayjs.Dayjs): void {
    this.report.set({ data: this.report().data, loading: true, error: false });
    this.transactionHistoryService
      .queryReport({
        'dateCreated.greaterThanOrEqual': start.toISOString(),
        'dateCreated.lessThanOrEqual': end.toISOString(),
      })
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: res => {
          this.report.set({ data: res.body, loading: false, error: false });
          this.breakdown.set(this.toBreakdown(res.body));
        },
        error: () => {
          this.report.set({ data: null, loading: false, error: true });
          this.breakdown.set([]);
        },
      });
  }

  private loadTopCustomers(start: dayjs.Dayjs, end: dayjs.Dayjs): void {
    this.topCustomers.set({ data: this.topCustomers().data, loading: true, error: false });
    this.dashboardService
      .topCustomers(start.format(BACKEND_DATE_FORMAT), end.format(BACKEND_DATE_FORMAT), TOP_N)
      .pipe(
        switchMap(rows => {
          if (rows.length === 0) {
            return of([] as ChartDatum[]);
          }
          return forkJoin(
            rows.map(row => {
              const value = Number(row.totalAmount) || 0;
              if (row.customerId == null) {
                return of({ name: 'Unknown', value });
              }
              return this.customerService.find(row.customerId).pipe(
                map(res => ({ name: this.customerName(res.body, row.customerId), value })),
                catchError(() => of({ name: `#${row.customerId}`, value })),
              );
            }),
          );
        }),
        takeUntilDestroyed(this.destroyRef),
      )
      .subscribe({
        next: data => this.topCustomers.set({ data, loading: false, error: false }),
        error: () => this.topCustomers.set({ data: [], loading: false, error: true }),
      });
  }

  private loadTrend(): void {
    this.reportService
      .getReport({ tag: 'RECEIVE' })
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: res => {
          const series = (res.body ?? [])
            .map(row => ({
              name: `${row.year}-${this.pad(row.month)}-${this.pad(row.day)}`,
              value: Number(row.totalAmount) || 0,
            }))
            .reverse() // backend orders newest-first; chart wants chronological
            .slice(-TREND_DAYS);
          this.trend.set({ data: [{ name: 'Received', series }], loading: false, error: false });
        },
        error: () => this.trend.set({ data: [], loading: false, error: true }),
      });
  }

  private toBreakdown(report: ITxnReport | null): ChartDatum[] {
    if (!report) {
      return [];
    }
    return [
      { name: 'X-Ray', value: report.xRayAmount || 0 },
      { name: 'Hallmark', value: report.hallMarkAmount || 0 },
      { name: 'Normal Melting', value: (report.normalMeltingAmount || 0) + (report.normalMeltingServiceChargeAmount || 0) },
      { name: 'Calc. Melting', value: (report.calculatedMeltingAmount || 0) + (report.calculatedMeltingServiceChargeAmount || 0) },
    ].filter(d => d.value > 0);
  }

  private customerName(customer: ICustomer | null, id?: number | null): string {
    const name = `${customer?.firstName ?? ''} ${customer?.lastName ?? ''}`.trim();
    return name || `#${id}`;
  }

  private pad(value?: string | number): string {
    return String(value ?? '').padStart(2, '0');
  }
}
