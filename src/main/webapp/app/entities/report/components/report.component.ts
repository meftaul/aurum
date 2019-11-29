import { Component, OnInit, OnDestroy, ViewChild, ElementRef } from '@angular/core';
import { JhiAlertService } from 'ng-jhipster';
// import * as moment from 'moment';
// import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ReportService } from 'app/entities/report/service-api/report.service';
import { Report } from 'app/entities/report/domain/report.model';
// import {Report} from "app/entities/report/domain/report.model";
import * as Plotly from 'plotly.js';

@Component({
  selector: 'jhi-aurum-voucher-viewer',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.scss']
})
export class ReportComponent implements OnInit, OnDestroy {
  // public graph;
  /*{
    data: [
      // { x: [1, 2, 3], y: [2, 6, 3], type: 'scatter', mode: 'lines+points', marker: {color: 'red'} },
      { x: [1, 2, 3], y: [2, 5, 3], type: 'bar' },
    ],
    layout: {title: 'A Daily Received Amount'}
  }*/

  @ViewChild('chart', { static: false }) el: ElementRef;

  reportData: Report[];

  constructor(protected jhiAlertService: JhiAlertService, protected reportService: ReportService) {}

  ngOnInit(): void {
    this.getReportData('RECEIVE');
  }

  getReportData(tag: string) {
    this.reportService.getReport({ tag }).subscribe(data => {
      const reportData = data.body;
      // eslint-disable-next-line no-console
      console.log(reportData);
      this.reportData = reportData;
      this.updatePlot();
    });
  }

  updatePlot() {
    const element = this.el.nativeElement;

    const data = [
      {
        x: [...this.reportData.map(r => r.day)],
        y: [...this.reportData.map(r => +r.totalAmount)],
        type: 'bar'
      }
    ];

    const layout = {
      title: 'Daily Collection',
      xaxis: {
        type: 'category'
      }
    };
    Plotly.plot(element, data, layout);
  }

  ngOnDestroy(): void {}
}
