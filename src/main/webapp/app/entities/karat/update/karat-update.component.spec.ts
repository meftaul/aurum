import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { KaratService } from '../service/karat.service';
import { IKarat } from '../karat.model';
import { KaratFormService } from './karat-form.service';

import { KaratUpdateComponent } from './karat-update.component';

describe('Karat Management Update Component', () => {
  let comp: KaratUpdateComponent;
  let fixture: ComponentFixture<KaratUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let karatFormService: KaratFormService;
  let karatService: KaratService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [KaratUpdateComponent],
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
      .overrideTemplate(KaratUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(KaratUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    karatFormService = TestBed.inject(KaratFormService);
    karatService = TestBed.inject(KaratService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const karat: IKarat = { id: 32675 };

      activatedRoute.data = of({ karat });
      comp.ngOnInit();

      expect(comp.karat).toEqual(karat);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IKarat>>();
      const karat = { id: 27109 };
      jest.spyOn(karatFormService, 'getKarat').mockReturnValue(karat);
      jest.spyOn(karatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ karat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: karat }));
      saveSubject.complete();

      // THEN
      expect(karatFormService.getKarat).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(karatService.update).toHaveBeenCalledWith(expect.objectContaining(karat));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IKarat>>();
      const karat = { id: 27109 };
      jest.spyOn(karatFormService, 'getKarat').mockReturnValue({ id: null });
      jest.spyOn(karatService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ karat: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: karat }));
      saveSubject.complete();

      // THEN
      expect(karatFormService.getKarat).toHaveBeenCalled();
      expect(karatService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IKarat>>();
      const karat = { id: 27109 };
      jest.spyOn(karatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ karat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(karatService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
