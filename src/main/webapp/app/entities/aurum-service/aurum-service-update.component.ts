import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IAurumService, AurumService } from 'app/shared/model/aurum-service.model';
import { AurumServiceService } from './aurum-service.service';
import { IVoucher } from 'app/shared/model/voucher.model';
import { VoucherService } from 'app/entities/voucher/voucher.service';

@Component({
  selector: 'jhi-aurum-service-update',
  templateUrl: './aurum-service-update.component.html',
})
export class AurumServiceUpdateComponent implements OnInit {
  isSaving = false;
  vouchers: IVoucher[] = [];

  editForm = this.fb.group({
    id: [],
    serviceType: [],
    itemName: [],
    quantity: [null, [Validators.min(0)]],
    weight: [null, [Validators.min(0)]],
    rate: [null, [Validators.min(0)]],
    amount: [],
    serviceName: [],
    karatType: [],
    expectedKaratType: [],
    addedAlloy: [],
    alloyQuantity: [null, [Validators.min(0)]],
    serviceCharge: [null, [Validators.min(0)]],
    freeCheck: [null, [Validators.min(0)]],
    hallMarkedText: [],
    weightOfFreeCheck: [],
    voucher: [],
  });

  constructor(
    protected aurumServiceService: AurumServiceService,
    protected voucherService: VoucherService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aurumService }) => {
      this.updateForm(aurumService);

      this.voucherService.query().subscribe((res: HttpResponse<IVoucher[]>) => (this.vouchers = res.body || []));
    });
  }

  updateForm(aurumService: IAurumService): void {
    this.editForm.patchValue({
      id: aurumService.id,
      serviceType: aurumService.serviceType,
      itemName: aurumService.itemName,
      quantity: aurumService.quantity,
      weight: aurumService.weight,
      rate: aurumService.rate,
      amount: aurumService.amount,
      serviceName: aurumService.serviceName,
      karatType: aurumService.karatType,
      expectedKaratType: aurumService.expectedKaratType,
      addedAlloy: aurumService.addedAlloy,
      alloyQuantity: aurumService.alloyQuantity,
      serviceCharge: aurumService.serviceCharge,
      freeCheck: aurumService.freeCheck,
      hallMarkedText: aurumService.hallMarkedText,
      weightOfFreeCheck: aurumService.weightOfFreeCheck,
      voucher: aurumService.voucher,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aurumService = this.createFromForm();
    if (aurumService.id !== undefined) {
      this.subscribeToSaveResponse(this.aurumServiceService.update(aurumService));
    } else {
      this.subscribeToSaveResponse(this.aurumServiceService.create(aurumService));
    }
  }

  private createFromForm(): IAurumService {
    return {
      ...new AurumService(),
      id: this.editForm.get(['id'])!.value,
      serviceType: this.editForm.get(['serviceType'])!.value,
      itemName: this.editForm.get(['itemName'])!.value,
      quantity: this.editForm.get(['quantity'])!.value,
      weight: this.editForm.get(['weight'])!.value,
      rate: this.editForm.get(['rate'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      serviceName: this.editForm.get(['serviceName'])!.value,
      karatType: this.editForm.get(['karatType'])!.value,
      expectedKaratType: this.editForm.get(['expectedKaratType'])!.value,
      addedAlloy: this.editForm.get(['addedAlloy'])!.value,
      alloyQuantity: this.editForm.get(['alloyQuantity'])!.value,
      serviceCharge: this.editForm.get(['serviceCharge'])!.value,
      freeCheck: this.editForm.get(['freeCheck'])!.value,
      hallMarkedText: this.editForm.get(['hallMarkedText'])!.value,
      weightOfFreeCheck: this.editForm.get(['weightOfFreeCheck'])!.value,
      voucher: this.editForm.get(['voucher'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAurumService>>): void {
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

  trackById(index: number, item: IVoucher): any {
    return item.id;
  }
}
