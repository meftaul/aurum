import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AurumSharedModule } from 'app/shared/shared.module';
import { AurumServiceComponent } from './aurum-service.component';
import { AurumServiceDetailComponent } from './aurum-service-detail.component';
import { AurumServiceUpdateComponent } from './aurum-service-update.component';
import { AurumServiceDeletePopupComponent, AurumServiceDeleteDialogComponent } from './aurum-service-delete-dialog.component';
import { aurumServiceRoute, aurumServicePopupRoute } from './aurum-service.route';

const ENTITY_STATES = [...aurumServiceRoute, ...aurumServicePopupRoute];

@NgModule({
  imports: [AurumSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    AurumServiceComponent,
    AurumServiceDetailComponent,
    AurumServiceUpdateComponent,
    AurumServiceDeleteDialogComponent,
    AurumServiceDeletePopupComponent
  ],
  entryComponents: [AurumServiceDeleteDialogComponent]
})
export class AurumAurumServiceModule {}
