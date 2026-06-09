import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { KaratDetailComponent } from './karat-detail.component';

describe('Karat Management Detail Component', () => {
  let comp: KaratDetailComponent;
  let fixture: ComponentFixture<KaratDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [KaratDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ karat: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(KaratDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(KaratDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load karat on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.karat).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
