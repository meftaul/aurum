import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IKarat, Karat } from 'app/shared/model/karat.model';
import { KaratService } from './karat.service';

@Component({
  selector: 'jhi-karat-update',
  templateUrl: './karat-update.component.html',
})
export class KaratUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    karatType: [],
    purityPercent: [null, [Validators.min(0)]],
  });

  constructor(protected karatService: KaratService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ karat }) => {
      this.updateForm(karat);
    });
  }

  updateForm(karat: IKarat): void {
    this.editForm.patchValue({
      id: karat.id,
      karatType: karat.karatType,
      purityPercent: karat.purityPercent,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const karat = this.createFromForm();
    if (karat.id !== undefined) {
      this.subscribeToSaveResponse(this.karatService.update(karat));
    } else {
      this.subscribeToSaveResponse(this.karatService.create(karat));
    }
  }

  private createFromForm(): IKarat {
    return {
      ...new Karat(),
      id: this.editForm.get(['id'])!.value,
      karatType: this.editForm.get(['karatType'])!.value,
      purityPercent: this.editForm.get(['purityPercent'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IKarat>>): void {
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
