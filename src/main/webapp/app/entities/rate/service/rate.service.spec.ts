import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRate } from '../rate.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../rate.test-samples';

import { RateService } from './rate.service';

const requireRestSample: IRate = {
  ...sampleWithRequiredData,
};

describe('Rate Service', () => {
  let service: RateService;
  let httpMock: HttpTestingController;
  let expectedResult: IRate | IRate[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RateService);
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

    it('should create a Rate', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const rate = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(rate).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Rate', () => {
      const rate = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(rate).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Rate', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Rate', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Rate', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRateToCollectionIfMissing', () => {
      it('should add a Rate to an empty array', () => {
        const rate: IRate = sampleWithRequiredData;
        expectedResult = service.addRateToCollectionIfMissing([], rate);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rate);
      });

      it('should not add a Rate to an array that contains it', () => {
        const rate: IRate = sampleWithRequiredData;
        const rateCollection: IRate[] = [
          {
            ...rate,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRateToCollectionIfMissing(rateCollection, rate);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Rate to an array that doesn't contain it", () => {
        const rate: IRate = sampleWithRequiredData;
        const rateCollection: IRate[] = [sampleWithPartialData];
        expectedResult = service.addRateToCollectionIfMissing(rateCollection, rate);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rate);
      });

      it('should add only unique Rate to an array', () => {
        const rateArray: IRate[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const rateCollection: IRate[] = [sampleWithRequiredData];
        expectedResult = service.addRateToCollectionIfMissing(rateCollection, ...rateArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const rate: IRate = sampleWithRequiredData;
        const rate2: IRate = sampleWithPartialData;
        expectedResult = service.addRateToCollectionIfMissing([], rate, rate2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rate);
        expect(expectedResult).toContain(rate2);
      });

      it('should accept null and undefined values', () => {
        const rate: IRate = sampleWithRequiredData;
        expectedResult = service.addRateToCollectionIfMissing([], null, rate, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rate);
      });

      it('should return initial array if no Rate is added', () => {
        const rateCollection: IRate[] = [sampleWithRequiredData];
        expectedResult = service.addRateToCollectionIfMissing(rateCollection, undefined, null);
        expect(expectedResult).toEqual(rateCollection);
      });
    });

    describe('compareRate', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRate(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRate(entity1, entity2);
        const compareResult2 = service.compareRate(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRate(entity1, entity2);
        const compareResult2 = service.compareRate(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRate(entity1, entity2);
        const compareResult2 = service.compareRate(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
