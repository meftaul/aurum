import { Component, OnInit, inject, input, output } from '@angular/core';
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

  private readonly profileService = inject(ProfileService);
  private readonly loginService = inject(LoginService);
  private readonly router = inject(Router);

  ngOnInit(): void {
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.openAPIEnabled = profileInfo.openAPIEnabled;
    });
  }

  onNavigate(): void {
    this.navigate.emit();
  }

  logout(): void {
    this.navigate.emit();
    this.loginService.logout();
    this.router.navigate(['']);
  }
}
