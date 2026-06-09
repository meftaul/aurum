import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TransactionHistoryService } from 'app/entities/transaction-history/service/transaction-history.service';
import { VoucherService } from 'app/entities/voucher/service/voucher.service';
import { CustomerDetailComponent } from './customer-detail.component';

describe('Customer Management Detail Component', () => {
  let comp: CustomerDetailComponent;
  let fixture: ComponentFixture<CustomerDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CustomerDetailComponent],
      providers: [
        provideHttpClient(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./customer-detail.component').then(m => m.CustomerDetailComponent),
              resolve: { customer: () => of({ id: 26915 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CustomerDetailComponent, '')
      .compileComponents();

    jest.spyOn(TestBed.inject(TransactionHistoryService), 'query').mockReturnValue(of(new HttpResponse({ body: [] })));
    jest.spyOn(TestBed.inject(VoucherService), 'query').mockReturnValue(of(new HttpResponse({ body: [] })));
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomerDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load customer on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CustomerDetailComponent);

      // THEN
      expect(instance.customer()).toEqual(expect.objectContaining({ id: 26915 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
