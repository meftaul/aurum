import { Routes } from '@angular/router';
import { ReportComponent } from 'app/entities/report/components/report.component';

export const REPORT_ROUTE: Routes = [
  {
    path: '',
    component: ReportComponent,
    data: {
      authorities: [],
      pageTitle: 'Report'
    }
  }
];
