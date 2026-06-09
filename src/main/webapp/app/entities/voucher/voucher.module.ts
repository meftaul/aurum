import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AurumSharedModule } from 'app/shared/shared.module';
import { VoucherComponent } from './voucher.component';
import { VoucherDetailComponent } from './voucher-detail.component';
import { VoucherUpdateComponent } from './voucher-update.component';
import { VoucherDeletePopupComponent, VoucherDeleteDialogComponent } from './voucher-delete-dialog.component';
import { voucherRoute, voucherPopupRoute } from './voucher.route';

const ENTITY_STATES = [...voucherRoute, ...voucherPopupRoute];

@NgModule({
  imports: [AurumSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    VoucherComponent,
    VoucherDetailComponent,
    VoucherUpdateComponent,
    VoucherDeleteDialogComponent,
    VoucherDeletePopupComponent
  ],
  entryComponents: [VoucherDeleteDialogComponent]
})
export class AurumVoucherModule {}
