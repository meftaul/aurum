import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../rate.test-samples';

import { RateFormService } from './rate-form.service';

describe('Rate Form Service', () => {
  let service: RateFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RateFormService);
  });

  describe('Service methods', () => {
    describe('createRateFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRateFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            rateType: expect.any(Object),
            unitPrice: expect.any(Object),
          })
        );
      });

      it('passing IRate should create a new form with FormGroup', () => {
        const formGroup = service.createRateFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            rateType: expect.any(Object),
            unitPrice: expect.any(Object),
          })
        );
      });
    });

    describe('getRate', () => {
      it('should return NewRate for default Rate initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createRateFormGroup(sampleWithNewData);

        const rate = service.getRate(formGroup) as any;

        expect(rate).toMatchObject(sampleWithNewData);
      });

      it('should return NewRate for empty Rate initial value', () => {
        const formGroup = service.createRateFormGroup();

        const rate = service.getRate(formGroup) as any;

        expect(rate).toMatchObject({});
      });

      it('should return IRate', () => {
        const formGroup = service.createRateFormGroup(sampleWithRequiredData);

        const rate = service.getRate(formGroup) as any;

        expect(rate).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRate should not enable id FormControl', () => {
        const formGroup = service.createRateFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRate should disable id FormControl', () => {
        const formGroup = service.createRateFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
