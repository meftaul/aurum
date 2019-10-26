import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAurumService } from 'app/shared/model/aurum-service.model';

@Component({
  selector: 'jhi-aurum-service-detail',
  templateUrl: './aurum-service-detail.component.html'
})
export class AurumServiceDetailComponent implements OnInit {
  aurumService: IAurumService;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ aurumService }) => {
      this.aurumService = aurumService;
    });
  }

  previousState() {
    window.history.back();
  }
}
