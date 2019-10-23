import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'customer',
        loadChildren: () => import('./customer/customer.module').then(m => m.AurumCustomerModule)
      },
      {
        path: 'voucher',
        loadChildren: () => import('./voucher/voucher.module').then(m => m.AurumVoucherModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class AurumEntityModule {}
