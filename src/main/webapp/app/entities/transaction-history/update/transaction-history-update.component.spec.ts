import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { TransactionHistoryService } from '../service/transaction-history.service';
import { ITransactionHistory } from '../transaction-history.model';
import { TransactionHistoryFormService } from './transaction-history-form.service';

import { TransactionHistoryUpdateComponent } from './transaction-history-update.component';

describe('TransactionHistory Management Update Component', () => {
  let comp: TransactionHistoryUpdateComponent;
  let fixture: ComponentFixture<TransactionHistoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let transactionHistoryFormService: TransactionHistoryFormService;
  let transactionHistoryService: TransactionHistoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TransactionHistoryUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(TransactionHistoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TransactionHistoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    transactionHistoryFormService = TestBed.inject(TransactionHistoryFormService);
    transactionHistoryService = TestBed.inject(TransactionHistoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const transactionHistory: ITransactionHistory = { id: 22968 };

      activatedRoute.data = of({ transactionHistory });
      comp.ngOnInit();

      expect(comp.transactionHistory).toEqual(transactionHistory);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransactionHistory>>();
      const transactionHistory = { id: 24341 };
      jest.spyOn(transactionHistoryFormService, 'getTransactionHistory').mockReturnValue(transactionHistory);
      jest.spyOn(transactionHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transactionHistory }));
      saveSubject.complete();

      // THEN
      expect(transactionHistoryFormService.getTransactionHistory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(transactionHistoryService.update).toHaveBeenCalledWith(expect.objectContaining(transactionHistory));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransactionHistory>>();
      const transactionHistory = { id: 24341 };
      jest.spyOn(transactionHistoryFormService, 'getTransactionHistory').mockReturnValue({ id: null });
      jest.spyOn(transactionHistoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionHistory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transactionHistory }));
      saveSubject.complete();

      // THEN
      expect(transactionHistoryFormService.getTransactionHistory).toHaveBeenCalled();
      expect(transactionHistoryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransactionHistory>>();
      const transactionHistory = { id: 24341 };
      jest.spyOn(transactionHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(transactionHistoryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
