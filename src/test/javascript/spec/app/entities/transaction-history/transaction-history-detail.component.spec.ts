import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AurumTestModule } from '../../../test.module';
import { TransactionHistoryDetailComponent } from 'app/entities/transaction-history/transaction-history-detail.component';
import { TransactionHistory } from 'app/shared/model/transaction-history.model';

describe('Component Tests', () => {
  describe('TransactionHistory Management Detail Component', () => {
    let comp: TransactionHistoryDetailComponent;
    let fixture: ComponentFixture<TransactionHistoryDetailComponent>;
    const route = ({ data: of({ transactionHistory: new TransactionHistory(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AurumTestModule],
        declarations: [TransactionHistoryDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(TransactionHistoryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TransactionHistoryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.transactionHistory).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
