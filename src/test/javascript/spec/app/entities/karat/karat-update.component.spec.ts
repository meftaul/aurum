import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AurumTestModule } from '../../../test.module';
import { KaratUpdateComponent } from 'app/entities/karat/karat-update.component';
import { KaratService } from 'app/entities/karat/karat.service';
import { Karat } from 'app/shared/model/karat.model';

describe('Component Tests', () => {
  describe('Karat Management Update Component', () => {
    let comp: KaratUpdateComponent;
    let fixture: ComponentFixture<KaratUpdateComponent>;
    let service: KaratService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AurumTestModule],
        declarations: [KaratUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(KaratUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(KaratUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(KaratService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Karat(123);
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
        const entity = new Karat();
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
