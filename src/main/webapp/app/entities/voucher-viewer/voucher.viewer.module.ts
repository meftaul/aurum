import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import SharedModule from 'app/shared/shared.module';
import { RouterModule } from '@angular/router';
import { VOUCHER_VIEWER_ROUTE } from './voucher.viewer.routes';
import { VoucherViewerComponent } from './components/voucher.viewer.component';

@NgModule({
  imports: [SharedModule, FormsModule, ReactiveFormsModule, RouterModule.forChild(VOUCHER_VIEWER_ROUTE)],
  declarations: [VoucherViewerComponent],
})
export class AurumVoucherViewerModule {}
