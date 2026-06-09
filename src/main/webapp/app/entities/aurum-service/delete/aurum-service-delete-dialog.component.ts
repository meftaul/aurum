import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAurumService } from '../aurum-service.model';
import { AurumServiceService } from '../service/aurum-service.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './aurum-service-delete-dialog.component.html',
})
export class AurumServiceDeleteDialogComponent {
  aurumService?: IAurumService;

  constructor(protected aurumServiceService: AurumServiceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aurumServiceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
