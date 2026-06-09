import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAurumService } from '../aurum-service.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../aurum-service.test-samples';

import { AurumServiceService } from './aurum-service.service';

const requireRestSample: IAurumService = {
  ...sampleWithRequiredData,
};

describe('AurumService Service', () => {
  let service: AurumServiceService;
  let httpMock: HttpTestingController;
  let expectedResult: IAurumService | IAurumService[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AurumServiceService);
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

    it('should create a AurumService', () => {
      const aurumService = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(aurumService).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AurumService', () => {
      const aurumService = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(aurumService).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AurumService', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AurumService', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AurumService', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAurumServiceToCollectionIfMissing', () => {
      it('should add a AurumService to an empty array', () => {
        const aurumService: IAurumService = sampleWithRequiredData;
        expectedResult = service.addAurumServiceToCollectionIfMissing([], aurumService);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aurumService);
      });

      it('should not add a AurumService to an array that contains it', () => {
        const aurumService: IAurumService = sampleWithRequiredData;
        const aurumServiceCollection: IAurumService[] = [
          {
            ...aurumService,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAurumServiceToCollectionIfMissing(aurumServiceCollection, aurumService);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AurumService to an array that doesn't contain it", () => {
        const aurumService: IAurumService = sampleWithRequiredData;
        const aurumServiceCollection: IAurumService[] = [sampleWithPartialData];
        expectedResult = service.addAurumServiceToCollectionIfMissing(aurumServiceCollection, aurumService);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aurumService);
      });

      it('should add only unique AurumService to an array', () => {
        const aurumServiceArray: IAurumService[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const aurumServiceCollection: IAurumService[] = [sampleWithRequiredData];
        expectedResult = service.addAurumServiceToCollectionIfMissing(aurumServiceCollection, ...aurumServiceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const aurumService: IAurumService = sampleWithRequiredData;
        const aurumService2: IAurumService = sampleWithPartialData;
        expectedResult = service.addAurumServiceToCollectionIfMissing([], aurumService, aurumService2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aurumService);
        expect(expectedResult).toContain(aurumService2);
      });

      it('should accept null and undefined values', () => {
        const aurumService: IAurumService = sampleWithRequiredData;
        expectedResult = service.addAurumServiceToCollectionIfMissing([], null, aurumService, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aurumService);
      });

      it('should return initial array if no AurumService is added', () => {
        const aurumServiceCollection: IAurumService[] = [sampleWithRequiredData];
        expectedResult = service.addAurumServiceToCollectionIfMissing(aurumServiceCollection, undefined, null);
        expect(expectedResult).toEqual(aurumServiceCollection);
      });
    });

    describe('compareAurumService', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAurumService(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 7727 };
        const entity2 = null;

        const compareResult1 = service.compareAurumService(entity1, entity2);
        const compareResult2 = service.compareAurumService(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 7727 };
        const entity2 = { id: 5287 };

        const compareResult1 = service.compareAurumService(entity1, entity2);
        const compareResult2 = service.compareAurumService(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 7727 };
        const entity2 = { id: 7727 };

        const compareResult1 = service.compareAurumService(entity1, entity2);
        const compareResult2 = service.compareAurumService(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
