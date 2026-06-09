import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { AurumServiceFormService, AurumServiceFormGroup } from './aurum-service-form.service';
import { IAurumService } from '../aurum-service.model';
import { AurumServiceService } from '../service/aurum-service.service';
import { IVoucher } from 'app/entities/voucher/voucher.model';
import { VoucherService } from 'app/entities/voucher/service/voucher.service';
import { Alloy } from 'app/entities/enumerations/alloy.model';

@Component({
  selector: 'jhi-aurum-service-update',
  templateUrl: './aurum-service-update.component.html',
})
export class AurumServiceUpdateComponent implements OnInit {
  isSaving = false;
  aurumService: IAurumService | null = null;
  alloyValues = Object.keys(Alloy);

  vouchersSharedCollection: IVoucher[] = [];

  editForm: AurumServiceFormGroup = this.aurumServiceFormService.createAurumServiceFormGroup();

  constructor(
    protected aurumServiceService: AurumServiceService,
    protected aurumServiceFormService: AurumServiceFormService,
    protected voucherService: VoucherService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareVoucher = (o1: IVoucher | null, o2: IVoucher | null): boolean => this.voucherService.compareVoucher(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aurumService }) => {
      this.aurumService = aurumService;
      if (aurumService) {
        this.updateForm(aurumService);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aurumService = this.aurumServiceFormService.getAurumService(this.editForm);
    if (aurumService.id !== null) {
      this.subscribeToSaveResponse(this.aurumServiceService.update(aurumService));
    } else {
      this.subscribeToSaveResponse(this.aurumServiceService.create(aurumService));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAurumService>>): void {
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

  protected updateForm(aurumService: IAurumService): void {
    this.aurumService = aurumService;
    this.aurumServiceFormService.resetForm(this.editForm, aurumService);

    this.vouchersSharedCollection = this.voucherService.addVoucherToCollectionIfMissing<IVoucher>(
      this.vouchersSharedCollection,
      aurumService.voucher
    );
  }

  protected loadRelationshipsOptions(): void {
    this.voucherService
      .query()
      .pipe(map((res: HttpResponse<IVoucher[]>) => res.body ?? []))
      .pipe(
        map((vouchers: IVoucher[]) => this.voucherService.addVoucherToCollectionIfMissing<IVoucher>(vouchers, this.aurumService?.voucher))
      )
      .subscribe((vouchers: IVoucher[]) => (this.vouchersSharedCollection = vouchers));
  }
}
