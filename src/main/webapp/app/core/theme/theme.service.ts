import { DOCUMENT } from '@angular/common';
import { Injectable, effect, inject, signal } from '@angular/core';

export type ThemeMode = 'light' | 'dark';

const STORAGE_KEY = 'aurum-theme';
const THEME_COLOR = { light: '#ffffff', dark: '#000000' } as const;

@Injectable({ providedIn: 'root' })
export class ThemeService {
  private readonly document = inject(DOCUMENT);

  readonly theme = signal<ThemeMode>(this.resolveInitial());

  constructor() {
    // Apply on construction and whenever the theme signal changes.
    effect(() => this.apply(this.theme()));
  }

  toggle(): void {
    this.theme.update(mode => (mode === 'dark' ? 'light' : 'dark'));
  }

  setTheme(mode: ThemeMode): void {
    this.theme.set(mode);
  }

  private resolveInitial(): ThemeMode {
    const saved = this.readStored();
    if (saved) {
      return saved;
    }
    const win = this.document.defaultView;
    if (win?.matchMedia('(prefers-color-scheme: dark)').matches) {
      return 'dark';
    }
    return 'light';
  }

  private readStored(): ThemeMode | null {
    try {
      const value = this.document.defaultView?.localStorage.getItem(STORAGE_KEY);
      return value === 'light' || value === 'dark' ? value : null;
    } catch {
      return null;
    }
  }

  private apply(mode: ThemeMode): void {
    this.document.documentElement.setAttribute('data-bs-theme', mode);
    try {
      this.document.defaultView?.localStorage.setItem(STORAGE_KEY, mode);
    } catch {
      // localStorage may be unavailable (private mode, SSR) — ignore.
    }
    this.document.querySelector('meta[name="theme-color"]')?.setAttribute('content', THEME_COLOR[mode]);
  }
}
