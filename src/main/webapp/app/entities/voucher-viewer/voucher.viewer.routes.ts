import { Routes } from '@angular/router';
import { VoucherViewerComponent } from './components/voucher.viewer.component';

export const VOUCHER_VIEWER_ROUTE: Routes = [
  {
    path: '',
    component: VoucherViewerComponent,
    data: {
      authorities: [],
      pageTitle: 'Aurum: Voucher Viewer'
    }
  }
];
