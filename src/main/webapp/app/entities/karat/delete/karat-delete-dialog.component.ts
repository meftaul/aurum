import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IKarat } from '../karat.model';
import { KaratService } from '../service/karat.service';

@Component({
  templateUrl: './karat-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class KaratDeleteDialogComponent {
  karat?: IKarat;

  protected karatService = inject(KaratService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.karatService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
