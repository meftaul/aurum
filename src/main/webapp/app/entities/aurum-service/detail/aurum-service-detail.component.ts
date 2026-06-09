import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAurumService } from '../aurum-service.model';

@Component({
  selector: 'jhi-aurum-service-detail',
  templateUrl: './aurum-service-detail.component.html',
})
export class AurumServiceDetailComponent implements OnInit {
  aurumService: IAurumService | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aurumService }) => {
      this.aurumService = aurumService;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
