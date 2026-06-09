import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AurumSharedModule } from 'app/shared/shared.module';
import { KaratComponent } from './karat.component';
import { KaratDetailComponent } from './karat-detail.component';
import { KaratUpdateComponent } from './karat-update.component';
import { KaratDeleteDialogComponent } from './karat-delete-dialog.component';
import { karatRoute } from './karat.route';

@NgModule({
  imports: [AurumSharedModule, RouterModule.forChild(karatRoute)],
  declarations: [KaratComponent, KaratDetailComponent, KaratUpdateComponent, KaratDeleteDialogComponent],
  entryComponents: [KaratDeleteDialogComponent],
})
export class AurumKaratModule {}
