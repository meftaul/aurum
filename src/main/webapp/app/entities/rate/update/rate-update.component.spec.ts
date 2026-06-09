import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { RateService } from '../service/rate.service';
import { IRate } from '../rate.model';
import { RateFormService } from './rate-form.service';

import { RateUpdateComponent } from './rate-update.component';

describe('Rate Management Update Component', () => {
  let comp: RateUpdateComponent;
  let fixture: ComponentFixture<RateUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let rateFormService: RateFormService;
  let rateService: RateService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RateUpdateComponent],
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
      .overrideTemplate(RateUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RateUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    rateFormService = TestBed.inject(RateFormService);
    rateService = TestBed.inject(RateService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const rate: IRate = { id: 3137 };

      activatedRoute.data = of({ rate });
      comp.ngOnInit();

      expect(comp.rate).toEqual(rate);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRate>>();
      const rate = { id: 15902 };
      jest.spyOn(rateFormService, 'getRate').mockReturnValue(rate);
      jest.spyOn(rateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rate }));
      saveSubject.complete();

      // THEN
      expect(rateFormService.getRate).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(rateService.update).toHaveBeenCalledWith(expect.objectContaining(rate));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRate>>();
      const rate = { id: 15902 };
      jest.spyOn(rateFormService, 'getRate').mockReturnValue({ id: null });
      jest.spyOn(rateService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rate: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rate }));
      saveSubject.complete();

      // THEN
      expect(rateFormService.getRate).toHaveBeenCalled();
      expect(rateService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRate>>();
      const rate = { id: 15902 };
      jest.spyOn(rateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(rateService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
