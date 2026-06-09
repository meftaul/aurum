import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AurumServiceFormService } from './aurum-service-form.service';
import { AurumServiceService } from '../service/aurum-service.service';
import { IAurumService } from '../aurum-service.model';
import { IVoucher } from 'app/entities/voucher/voucher.model';
import { VoucherService } from 'app/entities/voucher/service/voucher.service';

import { AurumServiceUpdateComponent } from './aurum-service-update.component';

describe('AurumService Management Update Component', () => {
  let comp: AurumServiceUpdateComponent;
  let fixture: ComponentFixture<AurumServiceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let aurumServiceFormService: AurumServiceFormService;
  let aurumServiceService: AurumServiceService;
  let voucherService: VoucherService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AurumServiceUpdateComponent],
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
      .overrideTemplate(AurumServiceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AurumServiceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    aurumServiceFormService = TestBed.inject(AurumServiceFormService);
    aurumServiceService = TestBed.inject(AurumServiceService);
    voucherService = TestBed.inject(VoucherService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Voucher query and add missing value', () => {
      const aurumService: IAurumService = { id: 456 };
      const voucher: IVoucher = { id: 13586 };
      aurumService.voucher = voucher;

      const voucherCollection: IVoucher[] = [{ id: 66553 }];
      jest.spyOn(voucherService, 'query').mockReturnValue(of(new HttpResponse({ body: voucherCollection })));
      const additionalVouchers = [voucher];
      const expectedCollection: IVoucher[] = [...additionalVouchers, ...voucherCollection];
      jest.spyOn(voucherService, 'addVoucherToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ aurumService });
      comp.ngOnInit();

      expect(voucherService.query).toHaveBeenCalled();
      expect(voucherService.addVoucherToCollectionIfMissing).toHaveBeenCalledWith(
        voucherCollection,
        ...additionalVouchers.map(expect.objectContaining)
      );
      expect(comp.vouchersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const aurumService: IAurumService = { id: 456 };
      const voucher: IVoucher = { id: 74801 };
      aurumService.voucher = voucher;

      activatedRoute.data = of({ aurumService });
      comp.ngOnInit();

      expect(comp.vouchersSharedCollection).toContain(voucher);
      expect(comp.aurumService).toEqual(aurumService);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAurumService>>();
      const aurumService = { id: 123 };
      jest.spyOn(aurumServiceFormService, 'getAurumService').mockReturnValue(aurumService);
      jest.spyOn(aurumServiceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aurumService });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aurumService }));
      saveSubject.complete();

      // THEN
      expect(aurumServiceFormService.getAurumService).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(aurumServiceService.update).toHaveBeenCalledWith(expect.objectContaining(aurumService));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAurumService>>();
      const aurumService = { id: 123 };
      jest.spyOn(aurumServiceFormService, 'getAurumService').mockReturnValue({ id: null });
      jest.spyOn(aurumServiceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aurumService: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aurumService }));
      saveSubject.complete();

      // THEN
      expect(aurumServiceFormService.getAurumService).toHaveBeenCalled();
      expect(aurumServiceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAurumService>>();
      const aurumService = { id: 123 };
      jest.spyOn(aurumServiceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aurumService });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(aurumServiceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareVoucher', () => {
      it('Should forward to voucherService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(voucherService, 'compareVoucher');
        comp.compareVoucher(entity, entity2);
        expect(voucherService.compareVoucher).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
