import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IKarat } from 'app/shared/model/karat.model';
import { KaratService } from './karat.service';

@Component({
  templateUrl: './karat-delete-dialog.component.html',
})
export class KaratDeleteDialogComponent {
  karat?: IKarat;

  constructor(protected karatService: KaratService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.karatService.delete(id).subscribe(() => {
      this.eventManager.broadcast('karatListModification');
      this.activeModal.close();
    });
  }
}
