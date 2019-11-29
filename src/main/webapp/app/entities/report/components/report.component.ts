import { Component, OnInit, OnDestroy } from '@angular/core';
import { JhiAlertService } from 'ng-jhipster';
// import * as moment from 'moment';
// import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ReportService } from 'app/entities/report/service-api/report.service';
// import {Report} from "app/entities/report/domain/report.model";

@Component({
  selector: 'jhi-aurum-voucher-viewer',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.scss']
})
export class ReportComponent implements OnInit, OnDestroy {
  constructor(protected jhiAlertService: JhiAlertService, protected reportService: ReportService) {}

  ngOnInit(): void {
    this.getReportData('RECEIVE');
  }

  getReportData(tag: string) {
    this.reportService.getReport({ tag }).subscribe(data => {
      const reportData = data.body;
      // eslint-disable-next-line no-console
      console.log(reportData);
    });
  }

  ngOnDestroy(): void {}
}
