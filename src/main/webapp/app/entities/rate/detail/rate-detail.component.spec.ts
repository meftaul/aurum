import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { RateDetailComponent } from './rate-detail.component';

describe('Rate Management Detail Component', () => {
  let comp: RateDetailComponent;
  let fixture: ComponentFixture<RateDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RateDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./rate-detail.component').then(m => m.RateDetailComponent),
              resolve: { rate: () => of({ id: 15902 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(RateDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RateDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load rate on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', RateDetailComponent);

      // THEN
      expect(instance.rate()).toEqual(expect.objectContaining({ id: 15902 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
