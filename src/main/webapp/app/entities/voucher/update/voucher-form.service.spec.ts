import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../voucher.test-samples';

import { VoucherFormService } from './voucher-form.service';

describe('Voucher Form Service', () => {
  let service: VoucherFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VoucherFormService);
  });

  describe('Service methods', () => {
    describe('createVoucherFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVoucherFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            voucherNo: expect.any(Object),
            customerId: expect.any(Object),
            calculatedTotalAmount: expect.any(Object),
            vat: expect.any(Object),
            disountAmount: expect.any(Object),
            status: expect.any(Object),
            totalPayableAmount: expect.any(Object),
            dateCreated: expect.any(Object),
            addedBy: expect.any(Object),
            boxNumber: expect.any(Object),
            deliveryDate: expect.any(Object),
            deliveryStatus: expect.any(Object),
          }),
        );
      });

      it('passing IVoucher should create a new form with FormGroup', () => {
        const formGroup = service.createVoucherFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            voucherNo: expect.any(Object),
            customerId: expect.any(Object),
            calculatedTotalAmount: expect.any(Object),
            vat: expect.any(Object),
            disountAmount: expect.any(Object),
            status: expect.any(Object),
            totalPayableAmount: expect.any(Object),
            dateCreated: expect.any(Object),
            addedBy: expect.any(Object),
            boxNumber: expect.any(Object),
            deliveryDate: expect.any(Object),
            deliveryStatus: expect.any(Object),
          }),
        );
      });
    });

    describe('getVoucher', () => {
      it('should return NewVoucher for default Voucher initial value', () => {
        const formGroup = service.createVoucherFormGroup(sampleWithNewData);

        const voucher = service.getVoucher(formGroup) as any;

        expect(voucher).toMatchObject(sampleWithNewData);
      });

      it('should return NewVoucher for empty Voucher initial value', () => {
        const formGroup = service.createVoucherFormGroup();

        const voucher = service.getVoucher(formGroup) as any;

        expect(voucher).toMatchObject({});
      });

      it('should return IVoucher', () => {
        const formGroup = service.createVoucherFormGroup(sampleWithRequiredData);

        const voucher = service.getVoucher(formGroup) as any;

        expect(voucher).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVoucher should not enable id FormControl', () => {
        const formGroup = service.createVoucherFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVoucher should disable id FormControl', () => {
        const formGroup = service.createVoucherFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
