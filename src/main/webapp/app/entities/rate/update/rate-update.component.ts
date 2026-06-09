import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { RateFormService, RateFormGroup } from './rate-form.service';
import { IRate } from '../rate.model';
import { RateService } from '../service/rate.service';

@Component({
  selector: 'jhi-rate-update',
  templateUrl: './rate-update.component.html',
})
export class RateUpdateComponent implements OnInit {
  isSaving = false;
  rate: IRate | null = null;

  editForm: RateFormGroup = this.rateFormService.createRateFormGroup();

  constructor(protected rateService: RateService, protected rateFormService: RateFormService, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rate }) => {
      this.rate = rate;
      if (rate) {
        this.updateForm(rate);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const rate = this.rateFormService.getRate(this.editForm);
    if (rate.id !== null) {
      this.subscribeToSaveResponse(this.rateService.update(rate));
    } else {
      this.subscribeToSaveResponse(this.rateService.create(rate));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRate>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(rate: IRate): void {
    this.rate = rate;
    this.rateFormService.resetForm(this.editForm, rate);
  }
}
