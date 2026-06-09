import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AurumTestModule } from '../../../test.module';
import { AurumServiceUpdateComponent } from 'app/entities/aurum-service/aurum-service-update.component';
import { AurumServiceService } from 'app/entities/aurum-service/aurum-service.service';
import { AurumService } from 'app/shared/model/aurum-service.model';

describe('Component Tests', () => {
  describe('AurumService Management Update Component', () => {
    let comp: AurumServiceUpdateComponent;
    let fixture: ComponentFixture<AurumServiceUpdateComponent>;
    let service: AurumServiceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AurumTestModule],
        declarations: [AurumServiceUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(AurumServiceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AurumServiceUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AurumServiceService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new AurumService(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new AurumService();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
