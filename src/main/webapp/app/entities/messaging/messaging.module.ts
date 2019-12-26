import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MatDatepickerModule } from '@angular/material/datepicker';

import { AurumSharedModule } from 'app/shared/shared.module';
import { MessagingComponent } from './components/messaging.component';
import { MESSAGING_ROUTE } from './messaging.routes';

@NgModule({
  imports: [AurumSharedModule, RouterModule.forChild(MESSAGING_ROUTE)],
  declarations: [MessagingComponent],
  providers: [MatDatepickerModule]
})
export class AurumMessagingModule {}
