import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IRate } from '../rate.model';
import { RateService } from '../service/rate.service';

@Component({
  templateUrl: './rate-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RateDeleteDialogComponent {
  rate?: IRate;

  protected rateService = inject(RateService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rateService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
