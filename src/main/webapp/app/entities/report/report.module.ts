import { NgModule } from '@angular/core';
import { AurumSharedModule } from 'app/shared/shared.module';
import { RouterModule } from '@angular/router';

import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { ReportComponent } from 'app/entities/report/components/report.component';
import { REPORT_ROUTE } from 'app/entities/report/report.routes';

import * as PlotlyJS from 'plotly.js/dist/plotly.js';
import { PlotlyModule } from 'angular-plotly.js';

PlotlyModule.plotlyjs = PlotlyJS;

@NgModule({
  imports: [
    AurumSharedModule,
    RouterModule.forChild(REPORT_ROUTE),
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatInputModule,
    MatFormFieldModule,
    PlotlyModule
    // MatDatepickerModule,
    // MatNativeDateModule,
    // MatTableModule,
    // MatSelectModule,
    // MatCheckboxModule
  ],
  declarations: [ReportComponent]
  // providers: [MatDatepickerModule],
  // entryComponents: [CustomerFormDialogComponent]
})
export class ReportModule {}
