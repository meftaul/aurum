import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { IVoucher, Voucher } from 'app/shared/model/voucher.model';
import { VoucherService } from './voucher.service';

@Component({
  selector: 'jhi-voucher-update',
  templateUrl: './voucher-update.component.html'
})
export class VoucherUpdateComponent implements OnInit {
  isSaving: boolean;
  dateCreatedDp: any;

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
    addedBy: [null, [Validators.required]]
  });

  constructor(protected voucherService: VoucherService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ voucher }) => {
      this.updateForm(voucher);
    });
  }

  updateForm(voucher: IVoucher) {
    this.editForm.patchValue({
      id: voucher.id,
      voucherNo: voucher.voucherNo,
      customerId: voucher.customerId,
      calculatedTotalAmount: voucher.calculatedTotalAmount,
      vat: voucher.vat,
      disountAmount: voucher.disountAmount,
      status: voucher.status,
      totalPayableAmount: voucher.totalPayableAmount,
      dateCreated: voucher.dateCreated,
      addedBy: voucher.addedBy
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
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
      id: this.editForm.get(['id']).value,
      voucherNo: this.editForm.get(['voucherNo']).value,
      customerId: this.editForm.get(['customerId']).value,
      calculatedTotalAmount: this.editForm.get(['calculatedTotalAmount']).value,
      vat: this.editForm.get(['vat']).value,
      disountAmount: this.editForm.get(['disountAmount']).value,
      status: this.editForm.get(['status']).value,
      totalPayableAmount: this.editForm.get(['totalPayableAmount']).value,
      dateCreated: this.editForm.get(['dateCreated']).value,
      addedBy: this.editForm.get(['addedBy']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVoucher>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
