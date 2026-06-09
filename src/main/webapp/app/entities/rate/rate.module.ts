import { NgModule } from '@angular/core';
import SharedModule from 'app/shared/shared.module';
import { RateComponent } from './list/rate.component';
import { RateDetailComponent } from './detail/rate-detail.component';
import { RateUpdateComponent } from './update/rate-update.component';
import { RateDeleteDialogComponent } from './delete/rate-delete-dialog.component';
import { RateRoutingModule } from './route/rate-routing.module';

@NgModule({
  imports: [SharedModule, RateRoutingModule],
  declarations: [RateComponent, RateDetailComponent, RateUpdateComponent, RateDeleteDialogComponent],
})
export class RateModule {}
