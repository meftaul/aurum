import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { AurumTestModule } from '../../../test.module';
import { TransactionHistoryDeleteDialogComponent } from 'app/entities/transaction-history/transaction-history-delete-dialog.component';
import { TransactionHistoryService } from 'app/entities/transaction-history/transaction-history.service';

describe('Component Tests', () => {
  describe('TransactionHistory Management Delete Component', () => {
    let comp: TransactionHistoryDeleteDialogComponent;
    let fixture: ComponentFixture<TransactionHistoryDeleteDialogComponent>;
    let service: TransactionHistoryService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AurumTestModule],
        declarations: [TransactionHistoryDeleteDialogComponent]
      })
        .overrideTemplate(TransactionHistoryDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TransactionHistoryDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TransactionHistoryService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
