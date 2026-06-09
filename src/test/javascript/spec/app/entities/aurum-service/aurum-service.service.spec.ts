import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AurumServiceService } from 'app/entities/aurum-service/aurum-service.service';
import { IAurumService, AurumService } from 'app/shared/model/aurum-service.model';
import { Alloy } from 'app/shared/model/enumerations/alloy.model';

describe('Service Tests', () => {
  describe('AurumService Service', () => {
    let injector: TestBed;
    let service: AurumServiceService;
    let httpMock: HttpTestingController;
    let elemDefault: IAurumService;
    let expectedResult: IAurumService | IAurumService[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(AurumServiceService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new AurumService(
        0,
        'AAAAAAA',
        'AAAAAAA',
        0,
        0,
        0,
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        Alloy.AU,
        0,
        0,
        0,
        'AAAAAAA',
        'AAAAAAA'
      );
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a AurumService', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new AurumService()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a AurumService', () => {
        const returnedFromService = Object.assign(
          {
            serviceType: 'BBBBBB',
            itemName: 'BBBBBB',
            quantity: 1,
            weight: 1,
            rate: 1,
            amount: 1,
            serviceName: 'BBBBBB',
            karatType: 'BBBBBB',
            expectedKaratType: 'BBBBBB',
            addedAlloy: 'BBBBBB',
            alloyQuantity: 1,
            serviceCharge: 1,
            freeCheck: 1,
            hallMarkedText: 'BBBBBB',
            weightOfFreeCheck: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of AurumService', () => {
        const returnedFromService = Object.assign(
          {
            serviceType: 'BBBBBB',
            itemName: 'BBBBBB',
            quantity: 1,
            weight: 1,
            rate: 1,
            amount: 1,
            serviceName: 'BBBBBB',
            karatType: 'BBBBBB',
            expectedKaratType: 'BBBBBB',
            addedAlloy: 'BBBBBB',
            alloyQuantity: 1,
            serviceCharge: 1,
            freeCheck: 1,
            hallMarkedText: 'BBBBBB',
            weightOfFreeCheck: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a AurumService', () => {
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
