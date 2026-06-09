import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ITransactionHistory } from '../transaction-history.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../transaction-history.test-samples';

import { RestTransactionHistory, TransactionHistoryService } from './transaction-history.service';

const requireRestSample: RestTransactionHistory = {
  ...sampleWithRequiredData,
  dateCreated: sampleWithRequiredData.dateCreated?.toJSON(),
};

describe('TransactionHistory Service', () => {
  let service: TransactionHistoryService;
  let httpMock: HttpTestingController;
  let expectedResult: ITransactionHistory | ITransactionHistory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TransactionHistoryService);
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

    it('should create a TransactionHistory', () => {
      const transactionHistory = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(transactionHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TransactionHistory', () => {
      const transactionHistory = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(transactionHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TransactionHistory', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TransactionHistory', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TransactionHistory', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTransactionHistoryToCollectionIfMissing', () => {
      it('should add a TransactionHistory to an empty array', () => {
        const transactionHistory: ITransactionHistory = sampleWithRequiredData;
        expectedResult = service.addTransactionHistoryToCollectionIfMissing([], transactionHistory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transactionHistory);
      });

      it('should not add a TransactionHistory to an array that contains it', () => {
        const transactionHistory: ITransactionHistory = sampleWithRequiredData;
        const transactionHistoryCollection: ITransactionHistory[] = [
          {
            ...transactionHistory,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTransactionHistoryToCollectionIfMissing(transactionHistoryCollection, transactionHistory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TransactionHistory to an array that doesn't contain it", () => {
        const transactionHistory: ITransactionHistory = sampleWithRequiredData;
        const transactionHistoryCollection: ITransactionHistory[] = [sampleWithPartialData];
        expectedResult = service.addTransactionHistoryToCollectionIfMissing(transactionHistoryCollection, transactionHistory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transactionHistory);
      });

      it('should add only unique TransactionHistory to an array', () => {
        const transactionHistoryArray: ITransactionHistory[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const transactionHistoryCollection: ITransactionHistory[] = [sampleWithRequiredData];
        expectedResult = service.addTransactionHistoryToCollectionIfMissing(transactionHistoryCollection, ...transactionHistoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const transactionHistory: ITransactionHistory = sampleWithRequiredData;
        const transactionHistory2: ITransactionHistory = sampleWithPartialData;
        expectedResult = service.addTransactionHistoryToCollectionIfMissing([], transactionHistory, transactionHistory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transactionHistory);
        expect(expectedResult).toContain(transactionHistory2);
      });

      it('should accept null and undefined values', () => {
        const transactionHistory: ITransactionHistory = sampleWithRequiredData;
        expectedResult = service.addTransactionHistoryToCollectionIfMissing([], null, transactionHistory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transactionHistory);
      });

      it('should return initial array if no TransactionHistory is added', () => {
        const transactionHistoryCollection: ITransactionHistory[] = [sampleWithRequiredData];
        expectedResult = service.addTransactionHistoryToCollectionIfMissing(transactionHistoryCollection, undefined, null);
        expect(expectedResult).toEqual(transactionHistoryCollection);
      });
    });

    describe('compareTransactionHistory', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTransactionHistory(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 24341 };
        const entity2 = null;

        const compareResult1 = service.compareTransactionHistory(entity1, entity2);
        const compareResult2 = service.compareTransactionHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 24341 };
        const entity2 = { id: 22968 };

        const compareResult1 = service.compareTransactionHistory(entity1, entity2);
        const compareResult2 = service.compareTransactionHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 24341 };
        const entity2 = { id: 24341 };

        const compareResult1 = service.compareTransactionHistory(entity1, entity2);
        const compareResult2 = service.compareTransactionHistory(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
