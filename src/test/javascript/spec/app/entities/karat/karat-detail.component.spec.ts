import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AurumTestModule } from '../../../test.module';
import { KaratDetailComponent } from 'app/entities/karat/karat-detail.component';
import { Karat } from 'app/shared/model/karat.model';

describe('Component Tests', () => {
  describe('Karat Management Detail Component', () => {
    let comp: KaratDetailComponent;
    let fixture: ComponentFixture<KaratDetailComponent>;
    const route = ({ data: of({ karat: new Karat(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AurumTestModule],
        declarations: [KaratDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(KaratDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(KaratDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.karat).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
