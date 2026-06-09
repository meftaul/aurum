import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAurumService } from 'app/shared/model/aurum-service.model';
import { AurumServiceService } from './aurum-service.service';

@Component({
  templateUrl: './aurum-service-delete-dialog.component.html',
})
export class AurumServiceDeleteDialogComponent {
  aurumService?: IAurumService;

  constructor(
    protected aurumServiceService: AurumServiceService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aurumServiceService.delete(id).subscribe(() => {
      this.eventManager.broadcast('aurumServiceListModification');
      this.activeModal.close();
    });
  }
}
