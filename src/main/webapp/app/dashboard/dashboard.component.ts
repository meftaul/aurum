import { Component } from '@angular/core';

import SharedModule from 'app/shared/shared.module';

@Component({
  selector: 'jhi-dashboard',
  standalone: true,
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
  imports: [SharedModule],
})
export default class DashboardComponent {}
