import { Component, effect, inject, input, signal } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { ItemCountComponent } from 'app/shared/pagination';
import { IVoucher } from 'app/entities/voucher/voucher.model';
import { VoucherService } from 'app/entities/voucher/service/voucher.service';
import { ICustomer } from '../customer.model';

const VOUCHERS_PER_PAGE = 10;

@Component({
  selector: 'jhi-customer-detail',
  templateUrl: './customer-detail.component.html',
  styleUrl: './customer-detail.component.scss',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe, ItemCountComponent],
})
export class CustomerDetailComponent {
  customer = input<ICustomer | null>(null);

  vouchers = signal<IVoucher[]>([]);
  isLoadingVouchers = signal(false);

  readonly itemsPerPage = VOUCHERS_PER_PAGE;
  voucherPage = signal(1);
  voucherTotalItems = signal(0);

  protected readonly voucherService = inject(VoucherService);

  constructor() {
    effect(() => {
      const id = this.customer()?.id;
      if (id != null) {
        this.voucherPage.set(1);
        this.loadVouchers(id, 1);
      } else {
        this.vouchers.set([]);
        this.voucherTotalItems.set(0);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  navigateVoucherPage(page: number): void {
    const id = this.customer()?.id;
    if (id == null) {
      return;
    }
    this.voucherPage.set(page);
    this.loadVouchers(id, page);
  }

  protected loadVouchers(customerId: number, page: number): void {
    this.isLoadingVouchers.set(true);
    const req = {
      'customerId.equals': customerId,
      page: page - 1,
      size: VOUCHERS_PER_PAGE,
      sort: ['dateCreated,desc'],
    };

    this.voucherService.query(req).subscribe({
      next: res => {
        this.vouchers.set(res.body ?? []);
        this.voucherTotalItems.set(Number(res.headers.get('X-Total-Count') ?? 0));
      },
      complete: () => this.isLoadingVouchers.set(false),
    });
  }
}
