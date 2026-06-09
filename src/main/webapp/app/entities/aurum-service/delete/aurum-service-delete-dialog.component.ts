import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAurumService } from '../aurum-service.model';
import { AurumServiceService } from '../service/aurum-service.service';

@Component({
  templateUrl: './aurum-service-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AurumServiceDeleteDialogComponent {
  aurumService?: IAurumService;

  protected aurumServiceService = inject(AurumServiceService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aurumServiceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
