import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IRate, Rate } from 'app/shared/model/rate.model';
import { RateService } from './rate.service';

@Component({
  selector: 'jhi-rate-update',
  templateUrl: './rate-update.component.html'
})
export class RateUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    rateType: [],
    unitPrice: []
  });

  constructor(protected rateService: RateService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ rate }) => {
      this.updateForm(rate);
    });
  }

  updateForm(rate: IRate) {
    this.editForm.patchValue({
      id: rate.id,
      rateType: rate.rateType,
      unitPrice: rate.unitPrice
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const rate = this.createFromForm();
    if (rate.id !== undefined) {
      this.subscribeToSaveResponse(this.rateService.update(rate));
    } else {
      this.subscribeToSaveResponse(this.rateService.create(rate));
    }
  }

  private createFromForm(): IRate {
    return {
      ...new Rate(),
      id: this.editForm.get(['id']).value,
      rateType: this.editForm.get(['rateType']).value,
      unitPrice: this.editForm.get(['unitPrice']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRate>>) {
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
