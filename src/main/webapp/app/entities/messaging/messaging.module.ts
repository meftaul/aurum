import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

import { SharedModule } from 'app/shared/shared.module';
import { MessagingComponent } from './components/messaging.component';
import { MESSAGING_ROUTE } from './messaging.routes';

@NgModule({
  imports: [SharedModule, MatFormFieldModule, MatInputModule, RouterModule.forChild(MESSAGING_ROUTE)],
  declarations: [MessagingComponent],
  providers: [MatDatepickerModule]
})
export class AurumMessagingModule {}
