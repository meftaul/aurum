import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AurumSharedModule } from 'app/shared/shared.module';
import { RateComponent } from './rate.component';
import { RateDetailComponent } from './rate-detail.component';
import { RateUpdateComponent } from './rate-update.component';
import { RateDeletePopupComponent, RateDeleteDialogComponent } from './rate-delete-dialog.component';
import { rateRoute, ratePopupRoute } from './rate.route';

const ENTITY_STATES = [...rateRoute, ...ratePopupRoute];

@NgModule({
  imports: [AurumSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [RateComponent, RateDetailComponent, RateUpdateComponent, RateDeleteDialogComponent, RateDeletePopupComponent],
  entryComponents: [RateDeleteDialogComponent]
})
export class AurumRateModule {}
