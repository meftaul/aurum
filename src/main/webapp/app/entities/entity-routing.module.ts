import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'customer',
        data: { pageTitle: 'Customers' },
        loadChildren: () => import('./customer/customer.module').then(m => m.CustomerModule),
      },
      {
        path: 'voucher',
        data: { pageTitle: 'Vouchers' },
        loadChildren: () => import('./voucher/voucher.module').then(m => m.VoucherModule),
      },
      {
        path: 'transaction-history',
        data: { pageTitle: 'TransactionHistories' },
        loadChildren: () => import('./transaction-history/transaction-history.module').then(m => m.TransactionHistoryModule),
      },
      {
        path: 'aurum-service',
        data: { pageTitle: 'AurumServices' },
        loadChildren: () => import('./aurum-service/aurum-service.module').then(m => m.AurumServiceModule),
      },
      {
        path: 'rate',
        data: { pageTitle: 'Rates' },
        loadChildren: () => import('./rate/rate.module').then(m => m.RateModule),
      },
      {
        path: 'karat',
        data: { pageTitle: 'Karats' },
        loadChildren: () => import('./karat/karat.module').then(m => m.KaratModule),
      },
      {
        path: 'item',
        data: { pageTitle: 'Items' },
        loadChildren: () => import('./item/item.module').then(m => m.ItemModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
