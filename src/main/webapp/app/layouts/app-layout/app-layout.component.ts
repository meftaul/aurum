import { Component, inject, signal } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ThemeService } from 'app/core/theme/theme.service';
import SidebarComponent from '../sidebar/sidebar.component';

const COLLAPSE_KEY = 'aurum-sidebar-collapsed';

function readStoredCollapsed(): boolean {
  try {
    return localStorage.getItem(COLLAPSE_KEY) === 'true';
  } catch {
    return false;
  }
}

@Component({
  selector: 'jhi-app-layout',
  standalone: true,
  templateUrl: './app-layout.component.html',
  styleUrl: './app-layout.component.scss',
  imports: [RouterModule, SharedModule, SidebarComponent],
})
export default class AppLayoutComponent {
  readonly theme = inject(ThemeService).theme;
  readonly collapsed = signal(readStoredCollapsed());
  readonly drawerOpen = signal(false);

  private readonly themeService = inject(ThemeService);

  toggleCollapse(): void {
    this.collapsed.update(c => {
      const next = !c;
      try {
        localStorage.setItem(COLLAPSE_KEY, String(next));
      } catch {
        // localStorage unavailable — ignore
      }
      return next;
    });
  }

  toggleDrawer(): void {
    this.drawerOpen.update(o => !o);
  }

  closeDrawer(): void {
    this.drawerOpen.set(false);
  }

  toggleTheme(): void {
    this.themeService.toggle();
  }
}
