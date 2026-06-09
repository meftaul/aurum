import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import SharedModule from 'app/shared/shared.module';
import { RouterModule } from '@angular/router';
import { VOUCHER_VIEWER_ROUTE } from './voucher.viewer.routes';
import { VoucherViewerComponent } from './components/voucher.viewer.component';

import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';

@NgModule({
  imports: [
    SharedModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forChild(VOUCHER_VIEWER_ROUTE),
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatInputModule,
    MatFormFieldModule,
    // MatDatepickerModule,
    // MatNativeDateModule,
    // MatTableModule,
    // MatSelectModule,
    // MatCheckboxModule
  ],
  declarations: [VoucherViewerComponent],
  // providers: [MatDatepickerModule],
  // entryComponents: [CustomerFormDialogComponent]
})
export class AurumVoucherViewerModule {}
