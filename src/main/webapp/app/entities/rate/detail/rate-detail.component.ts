import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IRate } from '../rate.model';

@Component({
  selector: 'jhi-rate-detail',
  templateUrl: './rate-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class RateDetailComponent {
  rate = input<IRate | null>(null);

  previousState(): void {
    window.history.back();
  }
}
