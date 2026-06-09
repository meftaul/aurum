import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IKarat } from 'app/shared/model/karat.model';

@Component({
  selector: 'jhi-karat-detail',
  templateUrl: './karat-detail.component.html',
})
export class KaratDetailComponent implements OnInit {
  karat: IKarat | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ karat }) => (this.karat = karat));
  }

  previousState(): void {
    window.history.back();
  }
}
