import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IVoucher, Voucher } from 'app/shared/model/voucher.model';
import { VoucherService } from './voucher.service';

@Component({
  selector: 'jhi-voucher-update',
  templateUrl: './voucher-update.component.html',
})
export class VoucherUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    voucherNo: [],
    customerId: [],
    calculatedTotalAmount: [null, [Validators.required]],
    vat: [],
    disountAmount: [],
    status: [null, [Validators.required]],
    totalPayableAmount: [null, [Validators.required]],
    dateCreated: [],
    addedBy: [null, [Validators.required]],
    boxNumber: [],
    deliveryDate: [],
    deliveryStatus: [],
  });

  constructor(protected voucherService: VoucherService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ voucher }) => {
      if (!voucher.id) {
        const today = moment().startOf('day');
        voucher.dateCreated = today;
        voucher.deliveryDate = today;
      }

      this.updateForm(voucher);
    });
  }

  updateForm(voucher: IVoucher): void {
    this.editForm.patchValue({
      id: voucher.id,
      voucherNo: voucher.voucherNo,
      customerId: voucher.customerId,
      calculatedTotalAmount: voucher.calculatedTotalAmount,
      vat: voucher.vat,
      disountAmount: voucher.disountAmount,
      status: voucher.status,
      totalPayableAmount: voucher.totalPayableAmount,
      dateCreated: voucher.dateCreated ? voucher.dateCreated.format(DATE_TIME_FORMAT) : null,
      addedBy: voucher.addedBy,
      boxNumber: voucher.boxNumber,
      deliveryDate: voucher.deliveryDate ? voucher.deliveryDate.format(DATE_TIME_FORMAT) : null,
      deliveryStatus: voucher.deliveryStatus,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const voucher = this.createFromForm();
    if (voucher.id !== undefined) {
      this.subscribeToSaveResponse(this.voucherService.update(voucher));
    } else {
      this.subscribeToSaveResponse(this.voucherService.create(voucher));
    }
  }

  private createFromForm(): IVoucher {
    return {
      ...new Voucher(),
      id: this.editForm.get(['id'])!.value,
      voucherNo: this.editForm.get(['voucherNo'])!.value,
      customerId: this.editForm.get(['customerId'])!.value,
      calculatedTotalAmount: this.editForm.get(['calculatedTotalAmount'])!.value,
      vat: this.editForm.get(['vat'])!.value,
      disountAmount: this.editForm.get(['disountAmount'])!.value,
      status: this.editForm.get(['status'])!.value,
      totalPayableAmount: this.editForm.get(['totalPayableAmount'])!.value,
      dateCreated: this.editForm.get(['dateCreated'])!.value
        ? moment(this.editForm.get(['dateCreated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      addedBy: this.editForm.get(['addedBy'])!.value,
      boxNumber: this.editForm.get(['boxNumber'])!.value,
      deliveryDate: this.editForm.get(['deliveryDate'])!.value
        ? moment(this.editForm.get(['deliveryDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      deliveryStatus: this.editForm.get(['deliveryStatus'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVoucher>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
