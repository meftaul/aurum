import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AurumSharedModule } from 'app/shared/shared.module';
import { VoucherComponent } from './voucher.component';
import { VoucherDetailComponent } from './voucher-detail.component';
import { VoucherUpdateComponent } from './voucher-update.component';
import { VoucherDeleteDialogComponent } from './voucher-delete-dialog.component';
import { voucherRoute } from './voucher.route';

@NgModule({
  imports: [AurumSharedModule, RouterModule.forChild(voucherRoute)],
  declarations: [VoucherComponent, VoucherDetailComponent, VoucherUpdateComponent, VoucherDeleteDialogComponent],
  entryComponents: [VoucherDeleteDialogComponent],
})
export class AurumVoucherModule {}
