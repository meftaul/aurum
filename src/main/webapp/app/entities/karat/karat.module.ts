import { NgModule } from '@angular/core';
import SharedModule from 'app/shared/shared.module';
import { KaratComponent } from './list/karat.component';
import { KaratDetailComponent } from './detail/karat-detail.component';
import { KaratUpdateComponent } from './update/karat-update.component';
import { KaratDeleteDialogComponent } from './delete/karat-delete-dialog.component';
import { KaratRoutingModule } from './route/karat-routing.module';

@NgModule({
  imports: [SharedModule, KaratRoutingModule],
  declarations: [KaratComponent, KaratDetailComponent, KaratUpdateComponent, KaratDeleteDialogComponent],
})
export class KaratModule {}
