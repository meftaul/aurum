import { Component } from '@angular/core';

import SharedModule from 'app/shared/shared.module';
import { RevealDirective } from 'app/shared/reveal/reveal.directive';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  imports: [SharedModule, RevealDirective],
})
export default class HomeComponent {}
