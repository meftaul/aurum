import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { InvoiceNewComponent } from './component-new/invoice.component';
// import { InvoiceComponent } from './components/invoice.component';

export const INVOICE_ROUTE: Routes = [
  {
    path: ':id',
    component: InvoiceNewComponent,
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_USER'],
      pageTitle: 'Invoice'
    },
    canActivate: [UserRouteAccessService]
  }
];
