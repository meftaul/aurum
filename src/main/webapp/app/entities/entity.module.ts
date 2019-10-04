import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
      { path: 'entity/transactions', loadChildren: () => import('./transaction/transaction.module').then(m => m.AurumTransactionModule) }
    ])
  ]
})
export class AurumEntityModule {}
