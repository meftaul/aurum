import { AfterViewInit, Directive, ElementRef, OnDestroy, inject } from '@angular/core';

/**
 * Scroll-reveal: fades/translates the host element in when it first enters the
 * viewport. The base `.reveal` class + transition live in global.scss; this
 * directive adds `is-visible` once intersecting. Respects prefers-reduced-motion
 * and degrades gracefully when IntersectionObserver is unavailable.
 */
@Directive({
  selector: '[jhiReveal]',
  standalone: true,
  host: { class: 'reveal' },
})
export class RevealDirective implements AfterViewInit, OnDestroy {
  private readonly el = inject(ElementRef<HTMLElement>);
  private observer: IntersectionObserver | null = null;

  ngAfterViewInit(): void {
    const host = this.el.nativeElement as HTMLElement;

    const prefersReducedMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
    if (prefersReducedMotion || typeof IntersectionObserver === 'undefined') {
      host.classList.add('is-visible');
      return;
    }

    // If the element is already within the viewport on init (e.g. above the fold
    // after a route change), reveal it right away so content is never stuck hidden.
    const rect = host.getBoundingClientRect();
    if (rect.top < window.innerHeight && rect.bottom > 0) {
      host.classList.add('is-visible');
      return;
    }

    this.observer = new IntersectionObserver(
      (entries, observer) => {
        entries.forEach(entry => {
          if (entry.isIntersecting) {
            host.classList.add('is-visible');
            observer.unobserve(entry.target);
          }
        });
      },
      { threshold: 0.15 },
    );
    this.observer.observe(host);
  }

  ngOnDestroy(): void {
    this.observer?.disconnect();
    this.observer = null;
  }
}
