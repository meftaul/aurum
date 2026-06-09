import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RateFormService } from './rate-form.service';
import { RateService } from '../service/rate.service';
import { IRate } from '../rate.model';

import { RateUpdateComponent } from './rate-update.component';

describe('Rate Management Update Component', () => {
  let comp: RateUpdateComponent;
  let fixture: ComponentFixture<RateUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let rateFormService: RateFormService;
  let rateService: RateService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RateUpdateComponent],
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
      .overrideTemplate(RateUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RateUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    rateFormService = TestBed.inject(RateFormService);
    rateService = TestBed.inject(RateService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const rate: IRate = { id: 456 };

      activatedRoute.data = of({ rate });
      comp.ngOnInit();

      expect(comp.rate).toEqual(rate);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRate>>();
      const rate = { id: 123 };
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

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRate>>();
      const rate = { id: 123 };
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

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRate>>();
      const rate = { id: 123 };
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
