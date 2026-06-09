import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import './vendor';
import { AurumSharedModule } from 'app/shared/shared.module';
import { AurumCoreModule } from 'app/core/core.module';
import { AurumAppRoutingModule } from './app-routing.module';
import { AurumHomeModule } from './home/home.module';
import { AurumEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AurumSharedModule,
    AurumCoreModule,
    AurumHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    AurumEntityModule,
    AurumAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent],
})
export class AurumAppModule {}
