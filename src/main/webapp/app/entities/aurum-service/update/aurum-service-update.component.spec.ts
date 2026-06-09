import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IVoucher } from 'app/entities/voucher/voucher.model';
import { VoucherService } from 'app/entities/voucher/service/voucher.service';
import { AurumServiceService } from '../service/aurum-service.service';
import { IAurumService } from '../aurum-service.model';
import { AurumServiceFormService } from './aurum-service-form.service';

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
      imports: [AurumServiceUpdateComponent],
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
    it('should call Voucher query and add missing value', () => {
      const aurumService: IAurumService = { id: 5287 };
      const voucher: IVoucher = { id: 9972 };
      aurumService.voucher = voucher;

      const voucherCollection: IVoucher[] = [{ id: 9972 }];
      jest.spyOn(voucherService, 'query').mockReturnValue(of(new HttpResponse({ body: voucherCollection })));
      const additionalVouchers = [voucher];
      const expectedCollection: IVoucher[] = [...additionalVouchers, ...voucherCollection];
      jest.spyOn(voucherService, 'addVoucherToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ aurumService });
      comp.ngOnInit();

      expect(voucherService.query).toHaveBeenCalled();
      expect(voucherService.addVoucherToCollectionIfMissing).toHaveBeenCalledWith(
        voucherCollection,
        ...additionalVouchers.map(expect.objectContaining),
      );
      expect(comp.vouchersSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const aurumService: IAurumService = { id: 5287 };
      const voucher: IVoucher = { id: 9972 };
      aurumService.voucher = voucher;

      activatedRoute.data = of({ aurumService });
      comp.ngOnInit();

      expect(comp.vouchersSharedCollection).toContainEqual(voucher);
      expect(comp.aurumService).toEqual(aurumService);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAurumService>>();
      const aurumService = { id: 7727 };
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

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAurumService>>();
      const aurumService = { id: 7727 };
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

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAurumService>>();
      const aurumService = { id: 7727 };
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
      it('should forward to voucherService', () => {
        const entity = { id: 9972 };
        const entity2 = { id: 7837 };
        jest.spyOn(voucherService, 'compareVoucher');
        comp.compareVoucher(entity, entity2);
        expect(voucherService.compareVoucher).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
