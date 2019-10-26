import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { AurumTestModule } from '../../../test.module';
import { AurumServiceDeleteDialogComponent } from 'app/entities/aurum-service/aurum-service-delete-dialog.component';
import { AurumServiceService } from 'app/entities/aurum-service/aurum-service.service';

describe('Component Tests', () => {
  describe('AurumService Management Delete Component', () => {
    let comp: AurumServiceDeleteDialogComponent;
    let fixture: ComponentFixture<AurumServiceDeleteDialogComponent>;
    let service: AurumServiceService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AurumTestModule],
        declarations: [AurumServiceDeleteDialogComponent]
      })
        .overrideTemplate(AurumServiceDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AurumServiceDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AurumServiceService);
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
