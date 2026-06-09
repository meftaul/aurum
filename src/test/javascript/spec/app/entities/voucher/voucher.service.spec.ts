import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { VoucherService } from 'app/entities/voucher/voucher.service';
import { IVoucher, Voucher } from 'app/shared/model/voucher.model';
import { VoucherStatus } from 'app/shared/model/enumerations/voucher-status.model';

describe('Service Tests', () => {
  describe('Voucher Service', () => {
    let injector: TestBed;
    let service: VoucherService;
    let httpMock: HttpTestingController;
    let elemDefault: IVoucher;
    let expectedResult: IVoucher | IVoucher[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(VoucherService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Voucher(0, 'AAAAAAA', 0, 0, 0, 0, VoucherStatus.PAID, 0, currentDate, 'AAAAAAA', 'AAAAAAA', currentDate, false);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateCreated: currentDate.format(DATE_TIME_FORMAT),
            deliveryDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Voucher', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateCreated: currentDate.format(DATE_TIME_FORMAT),
            deliveryDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateCreated: currentDate,
            deliveryDate: currentDate,
          },
          returnedFromService
        );

        service.create(new Voucher()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Voucher', () => {
        const returnedFromService = Object.assign(
          {
            voucherNo: 'BBBBBB',
            customerId: 1,
            calculatedTotalAmount: 1,
            vat: 1,
            disountAmount: 1,
            status: 'BBBBBB',
            totalPayableAmount: 1,
            dateCreated: currentDate.format(DATE_TIME_FORMAT),
            addedBy: 'BBBBBB',
            boxNumber: 'BBBBBB',
            deliveryDate: currentDate.format(DATE_TIME_FORMAT),
            deliveryStatus: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateCreated: currentDate,
            deliveryDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Voucher', () => {
        const returnedFromService = Object.assign(
          {
            voucherNo: 'BBBBBB',
            customerId: 1,
            calculatedTotalAmount: 1,
            vat: 1,
            disountAmount: 1,
            status: 'BBBBBB',
            totalPayableAmount: 1,
            dateCreated: currentDate.format(DATE_TIME_FORMAT),
            addedBy: 'BBBBBB',
            boxNumber: 'BBBBBB',
            deliveryDate: currentDate.format(DATE_TIME_FORMAT),
            deliveryStatus: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateCreated: currentDate,
            deliveryDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Voucher', () => {
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
