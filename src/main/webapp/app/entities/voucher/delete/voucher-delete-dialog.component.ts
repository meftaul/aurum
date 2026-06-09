import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVoucher } from '../voucher.model';
import { VoucherService } from '../service/voucher.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './voucher-delete-dialog.component.html',
})
export class VoucherDeleteDialogComponent {
  voucher?: IVoucher;

  constructor(protected voucherService: VoucherService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.voucherService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
