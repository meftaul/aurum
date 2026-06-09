import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

import NavbarComponent from '../navbar/navbar.component';
import FooterComponent from '../footer/footer.component';

@Component({
  selector: 'jhi-public-layout',
  standalone: true,
  templateUrl: './public-layout.component.html',
  imports: [RouterOutlet, NavbarComponent, FooterComponent],
})
export default class PublicLayoutComponent {}
