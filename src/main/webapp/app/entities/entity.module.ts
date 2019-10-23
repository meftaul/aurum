import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';

@NgModule({
  imports: [
    RouterModule.forChild([
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
      {
        path: 'entity/transactions',
        data: {
          authorities: ['ROLE_ADMIN', 'ROLE_USER']
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./transaction/transaction.module').then(m => m.AurumTransactionModule)
      }
    ])
  ]
})
export class AurumEntityModule {}
