import { NgModule } from '@angular/core';
import { AurumSharedModule } from 'app/shared/shared.module';
import { RouterModule } from '@angular/router';
import { VOUCHER_VIEWER_ROUTE } from './voucher.viewer.routes';
import { VoucherViewerComponent } from './components/voucher.viewer.component';

@NgModule({
  imports: [
    AurumSharedModule,
    RouterModule.forChild(VOUCHER_VIEWER_ROUTE)
    // MatButtonModule,
    // MatIconModule,
    // MatCardModule,
    // MatInputModule,
    // MatFormFieldModule,
    // MatDatepickerModule,
    // MatNativeDateModule,
    // MatTableModule,
    // MatSelectModule,
    // MatCheckboxModule
  ],
  declarations: [VoucherViewerComponent]
  // providers: [MatDatepickerModule],
  // entryComponents: [CustomerFormDialogComponent]
})
export class AurumVoucherViewerModule {}
