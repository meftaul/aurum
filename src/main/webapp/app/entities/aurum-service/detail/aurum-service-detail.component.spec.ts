import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AurumServiceDetailComponent } from './aurum-service-detail.component';

describe('AurumService Management Detail Component', () => {
  let comp: AurumServiceDetailComponent;
  let fixture: ComponentFixture<AurumServiceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AurumServiceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ aurumService: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AurumServiceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AurumServiceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load aurumService on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.aurumService).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
