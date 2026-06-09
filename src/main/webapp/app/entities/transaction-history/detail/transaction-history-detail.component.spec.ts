import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TransactionHistoryDetailComponent } from './transaction-history-detail.component';

describe('TransactionHistory Management Detail Component', () => {
  let comp: TransactionHistoryDetailComponent;
  let fixture: ComponentFixture<TransactionHistoryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TransactionHistoryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ transactionHistory: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TransactionHistoryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TransactionHistoryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load transactionHistory on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.transactionHistory).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
