import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IVoucher } from 'app/shared/model/voucher.model';
import { VoucherService } from './voucher.service';

@Component({
  templateUrl: './voucher-delete-dialog.component.html',
})
export class VoucherDeleteDialogComponent {
  voucher?: IVoucher;

  constructor(protected voucherService: VoucherService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.voucherService.delete(id).subscribe(() => {
      this.eventManager.broadcast('voucherListModification');
      this.activeModal.close();
    });
  }
}
