import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IAurumService } from '../aurum-service.model';

@Component({
  selector: 'jhi-aurum-service-detail',
  templateUrl: './aurum-service-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class AurumServiceDetailComponent {
  aurumService = input<IAurumService | null>(null);

  previousState(): void {
    window.history.back();
  }
}
