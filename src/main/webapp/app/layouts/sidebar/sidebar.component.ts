import { Component, OnInit, inject, input, output, signal } from '@angular/core';
import { Router, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import HasAnyAuthorityDirective from 'app/shared/auth/has-any-authority.directive';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/login/login.service';

@Component({
  selector: 'jhi-sidebar',
  standalone: true,
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss',
  imports: [RouterModule, SharedModule, HasAnyAuthorityDirective],
})
export default class SidebarComponent implements OnInit {
  readonly collapsed = input(false);
  readonly navigate = output();

  readonly account = inject(AccountService).trackCurrentAccount();
  inProduction = false;
  openAPIEnabled = false;

  /** Set of currently expanded accordion group ids. */
  private readonly openGroups = signal(new Set<string>());

  /** Route prefixes that belong to each accordion group — used to auto-open the active group on load. */
  private readonly groupRoutes: Record<string, string[]> = {
    operations: ['/transactions', '/voucher-viewer', '/messaging'],
    reports: ['/voucher', '/transaction-history'],
    catalog: ['/customer', '/item', '/rate', '/karat', '/aurum-service'],
    admin: ['/authority', '/admin', '/h2-console'],
  };

  private readonly profileService = inject(ProfileService);
  private readonly loginService = inject(LoginService);
  private readonly router = inject(Router);

  ngOnInit(): void {
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.openAPIEnabled = profileInfo.openAPIEnabled;
    });
    this.openActiveGroup();
  }

  isOpen(groupId: string): boolean {
    return this.openGroups().has(groupId);
  }

  toggleGroup(groupId: string): void {
    const next = new Set(this.openGroups());
    if (next.has(groupId)) {
      next.delete(groupId);
    } else {
      next.add(groupId);
    }
    this.openGroups.set(next);
  }

  onNavigate(): void {
    this.navigate.emit();
  }

  logout(): void {
    this.navigate.emit();
    this.loginService.logout();
    this.router.navigate(['']);
  }

  /** Operations is always open by default; whichever group owns the current URL is opened too. */
  private openActiveGroup(): void {
    const url = this.router.url;
    const next = new Set<string>(['operations']);
    for (const [groupId, routes] of Object.entries(this.groupRoutes)) {
      if (routes.some(route => url.startsWith(route))) {
        next.add(groupId);
      }
    }
    this.openGroups.set(next);
  }
}
