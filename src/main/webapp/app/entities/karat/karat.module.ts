import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AurumSharedModule } from 'app/shared/shared.module';
import { KaratComponent } from './karat.component';
import { KaratDetailComponent } from './karat-detail.component';
import { KaratUpdateComponent } from './karat-update.component';
import { KaratDeletePopupComponent, KaratDeleteDialogComponent } from './karat-delete-dialog.component';
import { karatRoute, karatPopupRoute } from './karat.route';

const ENTITY_STATES = [...karatRoute, ...karatPopupRoute];

@NgModule({
  imports: [AurumSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [KaratComponent, KaratDetailComponent, KaratUpdateComponent, KaratDeleteDialogComponent, KaratDeletePopupComponent],
  entryComponents: [KaratDeleteDialogComponent]
})
export class AurumKaratModule {}
