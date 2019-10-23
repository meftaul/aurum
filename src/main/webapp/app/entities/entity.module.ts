import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'entity/transactions',
        data: {
          authorities: ['ROLE_ADMIN', 'ROLE_USER']
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./transaction/transaction.module').then(m => m.AurumTransactionModule)
      },
      {
        path: 'customer',
        loadChildren: () => import('./customer/customer.module').then(m => m.AurumCustomerModule)
      },
      {
        path: 'voucher',
        loadChildren: () => import('./voucher/voucher.module').then(m => m.AurumVoucherModule)
      },
      {
        path: 'transaction-history',
        loadChildren: () => import('./transaction-history/transaction-history.module').then(m => m.AurumTransactionHistoryModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class AurumEntityModule {}
