import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AurumSharedModule } from 'app/shared/shared.module';
import { AurumServiceComponent } from './aurum-service.component';
import { AurumServiceDetailComponent } from './aurum-service-detail.component';
import { AurumServiceUpdateComponent } from './aurum-service-update.component';
import { AurumServiceDeleteDialogComponent } from './aurum-service-delete-dialog.component';
import { aurumServiceRoute } from './aurum-service.route';

@NgModule({
  imports: [AurumSharedModule, RouterModule.forChild(aurumServiceRoute)],
  declarations: [AurumServiceComponent, AurumServiceDetailComponent, AurumServiceUpdateComponent, AurumServiceDeleteDialogComponent],
  entryComponents: [AurumServiceDeleteDialogComponent],
})
export class AurumAurumServiceModule {}
