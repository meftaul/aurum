import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../karat.test-samples';

import { KaratFormService } from './karat-form.service';

describe('Karat Form Service', () => {
  let service: KaratFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(KaratFormService);
  });

  describe('Service methods', () => {
    describe('createKaratFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createKaratFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            karatType: expect.any(Object),
            purityPercent: expect.any(Object),
          }),
        );
      });

      it('passing IKarat should create a new form with FormGroup', () => {
        const formGroup = service.createKaratFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            karatType: expect.any(Object),
            purityPercent: expect.any(Object),
          }),
        );
      });
    });

    describe('getKarat', () => {
      it('should return NewKarat for default Karat initial value', () => {
        const formGroup = service.createKaratFormGroup(sampleWithNewData);

        const karat = service.getKarat(formGroup) as any;

        expect(karat).toMatchObject(sampleWithNewData);
      });

      it('should return NewKarat for empty Karat initial value', () => {
        const formGroup = service.createKaratFormGroup();

        const karat = service.getKarat(formGroup) as any;

        expect(karat).toMatchObject({});
      });

      it('should return IKarat', () => {
        const formGroup = service.createKaratFormGroup(sampleWithRequiredData);

        const karat = service.getKarat(formGroup) as any;

        expect(karat).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IKarat should not enable id FormControl', () => {
        const formGroup = service.createKaratFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewKarat should disable id FormControl', () => {
        const formGroup = service.createKaratFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
