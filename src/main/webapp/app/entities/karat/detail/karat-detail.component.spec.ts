import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { KaratDetailComponent } from './karat-detail.component';

describe('Karat Management Detail Component', () => {
  let comp: KaratDetailComponent;
  let fixture: ComponentFixture<KaratDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [KaratDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./karat-detail.component').then(m => m.KaratDetailComponent),
              resolve: { karat: () => of({ id: 27109 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(KaratDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(KaratDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load karat on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', KaratDetailComponent);

      // THEN
      expect(instance.karat()).toEqual(expect.objectContaining({ id: 27109 }));
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
