import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

import SharedModule from 'app/shared/shared.module';

@Component({
  selector: 'jhi-dashboard',
  standalone: true,
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
  imports: [SharedModule, RouterModule, FormsModule],
})
export default class DashboardComponent {
  voucherSearch = '';

  constructor(private router: Router) {}

  // Jump straight to the voucher viewer, pre-loading the entered voucher number.
  searchVoucher(): void {
    const voucherNo = this.voucherSearch?.trim();
    if (!voucherNo) {
      return;
    }
    this.router.navigate(['/voucher-viewer'], { queryParams: { voucherNo } });
  }
}
