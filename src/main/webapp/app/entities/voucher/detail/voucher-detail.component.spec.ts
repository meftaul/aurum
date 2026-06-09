import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VoucherDetailComponent } from './voucher-detail.component';

describe('Voucher Management Detail Component', () => {
  let comp: VoucherDetailComponent;
  let fixture: ComponentFixture<VoucherDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VoucherDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ voucher: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(VoucherDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(VoucherDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load voucher on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.voucher).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
