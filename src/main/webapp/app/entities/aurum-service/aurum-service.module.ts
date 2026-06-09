import { NgModule } from '@angular/core';
import SharedModule from 'app/shared/shared.module';
import { AurumServiceComponent } from './list/aurum-service.component';
import { AurumServiceDetailComponent } from './detail/aurum-service-detail.component';
import { AurumServiceUpdateComponent } from './update/aurum-service-update.component';
import { AurumServiceDeleteDialogComponent } from './delete/aurum-service-delete-dialog.component';
import { AurumServiceRoutingModule } from './route/aurum-service-routing.module';

@NgModule({
  imports: [SharedModule, AurumServiceRoutingModule],
  declarations: [AurumServiceComponent, AurumServiceDetailComponent, AurumServiceUpdateComponent, AurumServiceDeleteDialogComponent],
})
export class AurumServiceModule {}
