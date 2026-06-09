import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRate } from 'app/shared/model/rate.model';
import { RateService } from './rate.service';

@Component({
  templateUrl: './rate-delete-dialog.component.html',
})
export class RateDeleteDialogComponent {
  rate?: IRate;

  constructor(protected rateService: RateService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rateService.delete(id).subscribe(() => {
      this.eventManager.broadcast('rateListModification');
      this.activeModal.close();
    });
  }
}
