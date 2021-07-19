import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'transactions',
        data: {
          authorities: ['ROLE_ADMIN', 'ROLE_USER']
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./transaction/transaction.module').then(m => m.AurumTransactionModule)
      },
      {
        path: 'voucher-viewer',
        data: {
          authorities: ['ROLE_ADMIN', 'ROLE_USER']
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./voucher-viewer/voucher.viewer.module').then(m => m.AurumVoucherViewerModule)
      },
      {
        path: 'invoice',
        data: {
          authorities: ['ROLE_ADMIN', 'ROLE_USER']
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./invoice/invoice.module').then(m => m.AurumInvoiceModule)
      },
      {
        path: 'report',
        data: {
          authorities: ['ROLE_ADMIN']
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./report/report.module').then(m => m.ReportModule)
      },
      {
        path: 'customer',
        data: {
          authorities: ['ROLE_ADMIN', 'ROLE_USER']
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./customer/customer.module').then(m => m.AurumCustomerModule)
      },
      {
        path: 'voucher',
        data: {
          authorities: ['ROLE_ADMIN']
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./voucher/voucher.module').then(m => m.AurumVoucherModule)
      },
      {
        path: 'transaction-history',
        loadChildren: () => import('./transaction-history/transaction-history.module').then(m => m.AurumTransactionHistoryModule)
      },
      {
        path: 'aurum-service',
        loadChildren: () => import('./aurum-service/aurum-service.module').then(m => m.AurumAurumServiceModule)
      },
      {
        path: 'rate',
        loadChildren: () => import('./rate/rate.module').then(m => m.AurumRateModule)
      },
      {
        path: 'karat',
        loadChildren: () => import('./karat/karat.module').then(m => m.AurumKaratModule)
      },
      {
        path: 'item',
        loadChildren: () => import('./item/item.module').then(m => m.AurumItemModule)
      },
      {
        path: 'messaging',
        data: {
          authorities: ['ROLE_ADMIN']
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./messaging/messaging.module').then(m => m.AurumMessagingModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class AurumEntityModule {}
