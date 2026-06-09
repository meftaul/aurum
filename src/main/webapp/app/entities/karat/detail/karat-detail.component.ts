import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IKarat } from '../karat.model';

@Component({
  selector: 'jhi-karat-detail',
  templateUrl: './karat-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class KaratDetailComponent {
  karat = input<IKarat | null>(null);

  previousState(): void {
    window.history.back();
  }
}
