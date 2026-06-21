import { Component, inject } from '@angular/core';

import { AccountService } from 'app/core/auth/account.service';
import { Authority } from 'app/config/authority.constants';
import UserDashboardComponent from './user/user-dashboard.component';
import AdminDashboardComponent from './admin/admin-dashboard.component';

/**
 * Dashboard shell: renders the analytical (admin) or operational (user) variant based on the
 * logged-in role. Both variants live at the same /dashboard route.
 */
@Component({
  selector: 'jhi-dashboard',
  standalone: true,
  templateUrl: './dashboard.component.html',
  imports: [UserDashboardComponent, AdminDashboardComponent],
})
export default class DashboardComponent {
  protected readonly isAdmin = inject(AccountService).hasAnyAuthority(Authority.ADMIN);
}
