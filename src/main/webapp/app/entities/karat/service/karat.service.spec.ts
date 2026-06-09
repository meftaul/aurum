import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IKarat } from '../karat.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../karat.test-samples';

import { KaratService } from './karat.service';

const requireRestSample: IKarat = {
  ...sampleWithRequiredData,
};

describe('Karat Service', () => {
  let service: KaratService;
  let httpMock: HttpTestingController;
  let expectedResult: IKarat | IKarat[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(KaratService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Karat', () => {
      const karat = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(karat).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Karat', () => {
      const karat = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(karat).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Karat', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Karat', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Karat', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addKaratToCollectionIfMissing', () => {
      it('should add a Karat to an empty array', () => {
        const karat: IKarat = sampleWithRequiredData;
        expectedResult = service.addKaratToCollectionIfMissing([], karat);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(karat);
      });

      it('should not add a Karat to an array that contains it', () => {
        const karat: IKarat = sampleWithRequiredData;
        const karatCollection: IKarat[] = [
          {
            ...karat,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addKaratToCollectionIfMissing(karatCollection, karat);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Karat to an array that doesn't contain it", () => {
        const karat: IKarat = sampleWithRequiredData;
        const karatCollection: IKarat[] = [sampleWithPartialData];
        expectedResult = service.addKaratToCollectionIfMissing(karatCollection, karat);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(karat);
      });

      it('should add only unique Karat to an array', () => {
        const karatArray: IKarat[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const karatCollection: IKarat[] = [sampleWithRequiredData];
        expectedResult = service.addKaratToCollectionIfMissing(karatCollection, ...karatArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const karat: IKarat = sampleWithRequiredData;
        const karat2: IKarat = sampleWithPartialData;
        expectedResult = service.addKaratToCollectionIfMissing([], karat, karat2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(karat);
        expect(expectedResult).toContain(karat2);
      });

      it('should accept null and undefined values', () => {
        const karat: IKarat = sampleWithRequiredData;
        expectedResult = service.addKaratToCollectionIfMissing([], null, karat, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(karat);
      });

      it('should return initial array if no Karat is added', () => {
        const karatCollection: IKarat[] = [sampleWithRequiredData];
        expectedResult = service.addKaratToCollectionIfMissing(karatCollection, undefined, null);
        expect(expectedResult).toEqual(karatCollection);
      });
    });

    describe('compareKarat', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareKarat(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 27109 };
        const entity2 = null;

        const compareResult1 = service.compareKarat(entity1, entity2);
        const compareResult2 = service.compareKarat(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 27109 };
        const entity2 = { id: 32675 };

        const compareResult1 = service.compareKarat(entity1, entity2);
        const compareResult2 = service.compareKarat(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 27109 };
        const entity2 = { id: 27109 };

        const compareResult1 = service.compareKarat(entity1, entity2);
        const compareResult2 = service.compareKarat(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
