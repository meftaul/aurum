import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IKarat } from 'app/shared/model/karat.model';
import { KaratService } from './karat.service';

@Component({
  selector: 'jhi-karat-delete-dialog',
  templateUrl: './karat-delete-dialog.component.html'
})
export class KaratDeleteDialogComponent {
  karat: IKarat;

  constructor(protected karatService: KaratService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.karatService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'karatListModification',
        content: 'Deleted an karat'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-karat-delete-popup',
  template: ''
})
export class KaratDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ karat }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(KaratDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.karat = karat;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/karat', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/karat', { outlets: { popup: null } }]);
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
