import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TransactionHistoryFormService } from './transaction-history-form.service';
import { TransactionHistoryService } from '../service/transaction-history.service';
import { ITransactionHistory } from '../transaction-history.model';

import { TransactionHistoryUpdateComponent } from './transaction-history-update.component';

describe('TransactionHistory Management Update Component', () => {
  let comp: TransactionHistoryUpdateComponent;
  let fixture: ComponentFixture<TransactionHistoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let transactionHistoryFormService: TransactionHistoryFormService;
  let transactionHistoryService: TransactionHistoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TransactionHistoryUpdateComponent],
      providers: [
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
    it('Should update editForm', () => {
      const transactionHistory: ITransactionHistory = { id: 456 };

      activatedRoute.data = of({ transactionHistory });
      comp.ngOnInit();

      expect(comp.transactionHistory).toEqual(transactionHistory);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransactionHistory>>();
      const transactionHistory = { id: 123 };
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

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransactionHistory>>();
      const transactionHistory = { id: 123 };
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

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransactionHistory>>();
      const transactionHistory = { id: 123 };
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
