import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AurumTestModule } from '../../../test.module';
import { TransactionHistoryUpdateComponent } from 'app/entities/transaction-history/transaction-history-update.component';
import { TransactionHistoryService } from 'app/entities/transaction-history/transaction-history.service';
import { TransactionHistory } from 'app/shared/model/transaction-history.model';

describe('Component Tests', () => {
  describe('TransactionHistory Management Update Component', () => {
    let comp: TransactionHistoryUpdateComponent;
    let fixture: ComponentFixture<TransactionHistoryUpdateComponent>;
    let service: TransactionHistoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AurumTestModule],
        declarations: [TransactionHistoryUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(TransactionHistoryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TransactionHistoryUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TransactionHistoryService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new TransactionHistory(123);
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
        const entity = new TransactionHistory();
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
