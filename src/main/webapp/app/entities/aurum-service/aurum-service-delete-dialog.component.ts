import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAurumService } from 'app/shared/model/aurum-service.model';
import { AurumServiceService } from './aurum-service.service';

@Component({
  selector: 'jhi-aurum-service-delete-dialog',
  templateUrl: './aurum-service-delete-dialog.component.html'
})
export class AurumServiceDeleteDialogComponent {
  aurumService: IAurumService;

  constructor(
    protected aurumServiceService: AurumServiceService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.aurumServiceService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'aurumServiceListModification',
        content: 'Deleted an aurumService'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-aurum-service-delete-popup',
  template: ''
})
export class AurumServiceDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ aurumService }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(AurumServiceDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.aurumService = aurumService;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/aurum-service', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/aurum-service', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
