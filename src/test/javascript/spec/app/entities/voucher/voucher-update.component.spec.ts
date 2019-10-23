import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AurumTestModule } from '../../../test.module';
import { VoucherUpdateComponent } from 'app/entities/voucher/voucher-update.component';
import { VoucherService } from 'app/entities/voucher/voucher.service';
import { Voucher } from 'app/shared/model/voucher.model';

describe('Component Tests', () => {
  describe('Voucher Management Update Component', () => {
    let comp: VoucherUpdateComponent;
    let fixture: ComponentFixture<VoucherUpdateComponent>;
    let service: VoucherService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AurumTestModule],
        declarations: [VoucherUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(VoucherUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VoucherUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(VoucherService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Voucher(123);
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
        const entity = new Voucher();
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
