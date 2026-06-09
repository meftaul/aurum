import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../aurum-service.test-samples';

import { AurumServiceFormService } from './aurum-service-form.service';

describe('AurumService Form Service', () => {
  let service: AurumServiceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AurumServiceFormService);
  });

  describe('Service methods', () => {
    describe('createAurumServiceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAurumServiceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            serviceType: expect.any(Object),
            itemName: expect.any(Object),
            quantity: expect.any(Object),
            weight: expect.any(Object),
            rate: expect.any(Object),
            amount: expect.any(Object),
            serviceName: expect.any(Object),
            karatType: expect.any(Object),
            expectedKaratType: expect.any(Object),
            addedAlloy: expect.any(Object),
            alloyQuantity: expect.any(Object),
            serviceCharge: expect.any(Object),
            freeCheck: expect.any(Object),
            hallMarkedText: expect.any(Object),
            weightOfFreeCheck: expect.any(Object),
            voucher: expect.any(Object),
          }),
        );
      });

      it('passing IAurumService should create a new form with FormGroup', () => {
        const formGroup = service.createAurumServiceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            serviceType: expect.any(Object),
            itemName: expect.any(Object),
            quantity: expect.any(Object),
            weight: expect.any(Object),
            rate: expect.any(Object),
            amount: expect.any(Object),
            serviceName: expect.any(Object),
            karatType: expect.any(Object),
            expectedKaratType: expect.any(Object),
            addedAlloy: expect.any(Object),
            alloyQuantity: expect.any(Object),
            serviceCharge: expect.any(Object),
            freeCheck: expect.any(Object),
            hallMarkedText: expect.any(Object),
            weightOfFreeCheck: expect.any(Object),
            voucher: expect.any(Object),
          }),
        );
      });
    });

    describe('getAurumService', () => {
      it('should return NewAurumService for default AurumService initial value', () => {
        const formGroup = service.createAurumServiceFormGroup(sampleWithNewData);

        const aurumService = service.getAurumService(formGroup) as any;

        expect(aurumService).toMatchObject(sampleWithNewData);
      });

      it('should return NewAurumService for empty AurumService initial value', () => {
        const formGroup = service.createAurumServiceFormGroup();

        const aurumService = service.getAurumService(formGroup) as any;

        expect(aurumService).toMatchObject({});
      });

      it('should return IAurumService', () => {
        const formGroup = service.createAurumServiceFormGroup(sampleWithRequiredData);

        const aurumService = service.getAurumService(formGroup) as any;

        expect(aurumService).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAurumService should not enable id FormControl', () => {
        const formGroup = service.createAurumServiceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAurumService should disable id FormControl', () => {
        const formGroup = service.createAurumServiceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
