import { Component, effect, inject, input, signal } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ITransactionHistory } from 'app/entities/transaction-history/transaction-history.model';
import { TransactionHistoryService } from 'app/entities/transaction-history/service/transaction-history.service';
import { IVoucher } from 'app/entities/voucher/voucher.model';
import { VoucherService } from 'app/entities/voucher/service/voucher.service';
import { ICustomer } from '../customer.model';

const RELATED_ITEMS_SIZE = 10;

@Component({
  selector: 'jhi-customer-detail',
  templateUrl: './customer-detail.component.html',
  styleUrl: './customer-detail.component.scss',
  imports: [SharedModule, RouterModule],
})
export class CustomerDetailComponent {
  customer = input<ICustomer | null>(null);

  transactions = signal<ITransactionHistory[]>([]);
  vouchers = signal<IVoucher[]>([]);
  isLoadingRelated = signal(false);

  protected readonly transactionHistoryService = inject(TransactionHistoryService);
  protected readonly voucherService = inject(VoucherService);

  constructor() {
    effect(() => {
      const id = this.customer()?.id;
      if (id != null) {
        this.loadRelated(id);
      } else {
        this.transactions.set([]);
        this.vouchers.set([]);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  protected loadRelated(customerId: number): void {
    this.isLoadingRelated.set(true);
    const req = { 'customerId.equals': customerId, size: RELATED_ITEMS_SIZE, sort: ['dateCreated,desc'] };

    this.transactionHistoryService.query(req).subscribe({
      next: res => this.transactions.set(res.body ?? []),
    });

    this.voucherService.query(req).subscribe({
      next: res => this.vouchers.set(res.body ?? []),
      complete: () => this.isLoadingRelated.set(false),
    });
  }
}
