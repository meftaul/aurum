import { Component, inject } from '@angular/core';
import { Router, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { AccountService } from 'app/core/auth/account.service';
import { ThemeService } from 'app/core/theme/theme.service';

@Component({
  selector: 'jhi-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss',
  imports: [RouterModule, SharedModule],
})
export default class NavbarComponent {
  account = inject(AccountService).trackCurrentAccount();
  theme = inject(ThemeService).theme;

  private readonly themeService = inject(ThemeService);
  private readonly router = inject(Router);

  login(): void {
    this.router.navigate(['/login']);
  }

  toggleTheme(): void {
    this.themeService.toggle();
  }
}
