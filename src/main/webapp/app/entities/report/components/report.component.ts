import { Component, OnInit, OnDestroy } from '@angular/core';
import { JhiAlertService } from 'ng-jhipster';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

@Component({
  selector: 'jhi-aurum-voucher-viewer',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.scss']
})
export class ReportComponent implements OnInit, OnDestroy {
  constructor(protected jhiAlertService: JhiAlertService) {}

  ngOnInit(): void {}

  ngOnDestroy(): void {}
}
