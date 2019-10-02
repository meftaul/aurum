import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

import { AurumSharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';

@NgModule({
  imports: [AurumSharedModule, RouterModule.forChild([HOME_ROUTE]), MatButtonModule, MatIconModule],
  declarations: [HomeComponent]
})
export class AurumHomeModule {}
