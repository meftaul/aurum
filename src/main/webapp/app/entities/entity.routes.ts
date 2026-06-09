import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'Authorities' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'customer',
    data: { pageTitle: 'Customers' },
    loadChildren: () => import('./customer/customer.routes'),
  },
  {
    path: 'voucher',
    data: { pageTitle: 'Vouchers' },
    loadChildren: () => import('./voucher/voucher.routes'),
  },
  {
    path: 'transaction-history',
    data: { pageTitle: 'TransactionHistories' },
    loadChildren: () => import('./transaction-history/transaction-history.routes'),
  },
  {
    path: 'aurum-service',
    data: { pageTitle: 'AurumServices' },
    loadChildren: () => import('./aurum-service/aurum-service.routes'),
  },
  {
    path: 'rate',
    data: { pageTitle: 'Rates' },
    loadChildren: () => import('./rate/rate.routes'),
  },
  {
    path: 'karat',
    data: { pageTitle: 'Karats' },
    loadChildren: () => import('./karat/karat.routes'),
  },
  {
    path: 'item',
    data: { pageTitle: 'Items' },
    loadChildren: () => import('./item/item.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
