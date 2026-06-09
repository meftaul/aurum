import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AurumTestModule } from '../../../test.module';
import { AurumServiceDetailComponent } from 'app/entities/aurum-service/aurum-service-detail.component';
import { AurumService } from 'app/shared/model/aurum-service.model';

describe('Component Tests', () => {
  describe('AurumService Management Detail Component', () => {
    let comp: AurumServiceDetailComponent;
    let fixture: ComponentFixture<AurumServiceDetailComponent>;
    const route = ({ data: of({ aurumService: new AurumService(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AurumTestModule],
        declarations: [AurumServiceDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(AurumServiceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AurumServiceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.aurumService).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
