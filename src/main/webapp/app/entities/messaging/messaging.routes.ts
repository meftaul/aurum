import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MessagingComponent } from './components/messaging.component';

export const MESSAGING_ROUTE: Routes = [
  {
    path: '',
    component: MessagingComponent,
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_USER'],
      pageTitle: 'Messaging'
    },
    canActivate: [UserRouteAccessService]
  }
];
