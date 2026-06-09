import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { KaratFormService, KaratFormGroup } from './karat-form.service';
import { IKarat } from '../karat.model';
import { KaratService } from '../service/karat.service';

@Component({
  selector: 'jhi-karat-update',
  templateUrl: './karat-update.component.html',
})
export class KaratUpdateComponent implements OnInit {
  isSaving = false;
  karat: IKarat | null = null;

  editForm: KaratFormGroup = this.karatFormService.createKaratFormGroup();

  constructor(
    protected karatService: KaratService,
    protected karatFormService: KaratFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ karat }) => {
      this.karat = karat;
      if (karat) {
        this.updateForm(karat);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const karat: any = this.karatFormService.getKarat(this.editForm);
    if (karat.id !== null) {
      this.subscribeToSaveResponse(this.karatService.update(karat));
    } else {
      this.subscribeToSaveResponse(this.karatService.create(karat));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IKarat>>): void {
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

  protected updateForm(karat: IKarat): void {
    this.karat = karat;
    this.karatFormService.resetForm(this.editForm, karat);
  }
}
