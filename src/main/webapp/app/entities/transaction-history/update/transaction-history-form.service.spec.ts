import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../transaction-history.test-samples';

import { TransactionHistoryFormService } from './transaction-history-form.service';

describe('TransactionHistory Form Service', () => {
  let service: TransactionHistoryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TransactionHistoryFormService);
  });

  describe('Service methods', () => {
    describe('createTransactionHistoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTransactionHistoryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            voucherNo: expect.any(Object),
            amount: expect.any(Object),
            dateCreated: expect.any(Object),
            tag: expect.any(Object),
            customerId: expect.any(Object),
            addedBy: expect.any(Object),
          })
        );
      });

      it('passing ITransactionHistory should create a new form with FormGroup', () => {
        const formGroup = service.createTransactionHistoryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            voucherNo: expect.any(Object),
            amount: expect.any(Object),
            dateCreated: expect.any(Object),
            tag: expect.any(Object),
            customerId: expect.any(Object),
            addedBy: expect.any(Object),
          })
        );
      });
    });

    describe('getTransactionHistory', () => {
      it('should return NewTransactionHistory for default TransactionHistory initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createTransactionHistoryFormGroup(sampleWithNewData);

        const transactionHistory = service.getTransactionHistory(formGroup) as any;

        expect(transactionHistory).toMatchObject(sampleWithNewData);
      });

      it('should return NewTransactionHistory for empty TransactionHistory initial value', () => {
        const formGroup = service.createTransactionHistoryFormGroup();

        const transactionHistory = service.getTransactionHistory(formGroup) as any;

        expect(transactionHistory).toMatchObject({});
      });

      it('should return ITransactionHistory', () => {
        const formGroup = service.createTransactionHistoryFormGroup(sampleWithRequiredData);

        const transactionHistory = service.getTransactionHistory(formGroup) as any;

        expect(transactionHistory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITransactionHistory should not enable id FormControl', () => {
        const formGroup = service.createTransactionHistoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTransactionHistory should disable id FormControl', () => {
        const formGroup = service.createTransactionHistoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
