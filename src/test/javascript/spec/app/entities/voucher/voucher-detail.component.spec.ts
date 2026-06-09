import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AurumTestModule } from '../../../test.module';
import { VoucherDetailComponent } from 'app/entities/voucher/voucher-detail.component';
import { Voucher } from 'app/shared/model/voucher.model';

describe('Component Tests', () => {
  describe('Voucher Management Detail Component', () => {
    let comp: VoucherDetailComponent;
    let fixture: ComponentFixture<VoucherDetailComponent>;
    const route = ({ data: of({ voucher: new Voucher(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AurumTestModule],
        declarations: [VoucherDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(VoucherDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(VoucherDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.voucher).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
