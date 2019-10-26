import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { TransactionHistoryService } from 'app/entities/transaction-history/transaction-history.service';
import { ITransactionHistory, TransactionHistory } from 'app/shared/model/transaction-history.model';
import { TransactionStatus } from 'app/shared/model/enumerations/transaction-status.model';

describe('Service Tests', () => {
  describe('TransactionHistory Service', () => {
    let injector: TestBed;
    let service: TransactionHistoryService;
    let httpMock: HttpTestingController;
    let elemDefault: ITransactionHistory;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(TransactionHistoryService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new TransactionHistory(0, 'AAAAAAA', 0, currentDate, TransactionStatus.RECEIVE, 0, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateCreated: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a TransactionHistory', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateCreated: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dateCreated: currentDate
          },
          returnedFromService
        );
        service
          .create(new TransactionHistory(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a TransactionHistory', () => {
        const returnedFromService = Object.assign(
          {
            voucherNo: 'BBBBBB',
            amount: 1,
            dateCreated: currentDate.format(DATE_FORMAT),
            tag: 'BBBBBB',
            customerId: 1,
            addedBy: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateCreated: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of TransactionHistory', () => {
        const returnedFromService = Object.assign(
          {
            voucherNo: 'BBBBBB',
            amount: 1,
            dateCreated: currentDate.format(DATE_FORMAT),
            tag: 'BBBBBB',
            customerId: 1,
            addedBy: 'BBBBBB'
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dateCreated: currentDate
          },
          returnedFromService
        );
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a TransactionHistory', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
