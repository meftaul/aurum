import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material';
import { MatTableModule } from '@angular/material/table';
import { MatSelectModule } from '@angular/material/select';
import { MatCheckboxModule } from '@angular/material/checkbox';

import { AurumSharedModule } from 'app/shared/shared.module';
import { VoucherComponent } from './voucher.component';
import { VoucherDetailComponent } from './voucher-detail.component';
import { VoucherUpdateComponent } from './voucher-update.component';
import { VoucherDeletePopupComponent, VoucherDeleteDialogComponent } from './voucher-delete-dialog.component';
import { voucherRoute, voucherPopupRoute } from './voucher.route';

const ENTITY_STATES = [...voucherRoute, ...voucherPopupRoute];

@NgModule({
  imports: [
    AurumSharedModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatInputModule,
    MatFormFieldModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatTableModule,
    MatSelectModule,
    MatCheckboxModule,
    RouterModule.forChild(ENTITY_STATES)
  ],
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
