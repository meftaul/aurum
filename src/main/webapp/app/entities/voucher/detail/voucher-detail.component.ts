import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVoucher } from '../voucher.model';

@Component({
  selector: 'jhi-voucher-detail',
  templateUrl: './voucher-detail.component.html',
})
export class VoucherDetailComponent implements OnInit {
  voucher: IVoucher | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ voucher }) => {
      this.voucher = voucher;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
