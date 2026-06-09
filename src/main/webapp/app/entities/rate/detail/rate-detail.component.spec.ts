import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RateDetailComponent } from './rate-detail.component';

describe('Rate Management Detail Component', () => {
  let comp: RateDetailComponent;
  let fixture: ComponentFixture<RateDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RateDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ rate: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RateDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RateDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load rate on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.rate).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
