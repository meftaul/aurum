import { Component, ElementRef, HostListener, NgZone, OnInit, ViewChild, inject, signal } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { Observable, Subscription, combineLatest, filter, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { ItemCountComponent } from 'app/shared/pagination';
import { FormsModule } from '@angular/forms';
import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { IVoucher } from '../voucher.model';

import { EntityArrayResponseType, VoucherService } from '../service/voucher.service';
import { VoucherDeleteDialogComponent } from '../delete/voucher-delete-dialog.component';
import { AlertService } from 'app/core/util/alert.service';

interface VoucherSearchCriteria {
  voucherNo: string;
  customerId: string;
  status: string;
  boxNumber: string;
}

type VoucherSearchField = keyof VoucherSearchCriteria;

const SEARCH_FIELDS: VoucherSearchField[] = ['voucherNo', 'customerId', 'status', 'boxNumber'];

// Fields revealed by the "Advanced" disclosure (voucherNo is the always-visible primary search).
const ADVANCED_FIELDS: VoucherSearchField[] = ['customerId', 'status', 'boxNumber'];

const SEARCH_FIELD_LABELS: Record<VoucherSearchField, string> = {
  voucherNo: 'Voucher No',
  customerId: 'Customer Id',
  status: 'Status',
  boxNumber: 'Box Number',
};

// Map each criterion to its JHipster query param (operators differ per field type).
const SEARCH_FIELD_PARAM: Record<VoucherSearchField, string> = {
  voucherNo: 'voucherNo.contains',
  customerId: 'customerId.equals',
  status: 'status.equals',
  boxNumber: 'boxNumber.contains',
};

const emptyCriteria = (): VoucherSearchCriteria => ({ voucherNo: '', customerId: '', status: '', boxNumber: '' });

@Component({
  selector: 'jhi-voucher',
  templateUrl: './voucher.component.html',
  styleUrl: './voucher.component.scss',
  imports: [RouterModule, FormsModule, SharedModule, SortDirective, SortByDirective, FormatMediumDatetimePipe, ItemCountComponent],
})
export class VoucherComponent implements OnInit {
  subscription: Subscription | null = null;
  vouchers = signal<IVoucher[]>([]);
  isLoading = false;

  sortState = sortStateSignal({});
  searchCriteria: VoucherSearchCriteria = emptyCriteria();
  showAdvanced = false;

  @ViewChild('searchInput') searchInput?: ElementRef<HTMLInputElement>;

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  public readonly router = inject(Router);
  protected readonly voucherService = inject(VoucherService);
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);
  protected alertService = inject(AlertService);

  trackId = (item: IVoucher): number => this.voucherService.getVoucherIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();
  }

  search(): void {
    this.handleNavigation(1, this.sortState());
  }

  clearSearch(): void {
    this.searchCriteria = emptyCriteria();
    this.handleNavigation(1, this.sortState());
  }

  hasActiveSearch(): boolean {
    return SEARCH_FIELDS.some(field => this.searchCriteria[field].trim() !== '');
  }

  toggleAdvanced(): void {
    this.showAdvanced = !this.showAdvanced;
  }

  /** Active criteria as removable chips (label + trimmed value), in field order. */
  activeFilters(): { key: VoucherSearchField; label: string; value: string }[] {
    return SEARCH_FIELDS.filter(field => this.searchCriteria[field].trim() !== '').map(field => ({
      key: field,
      label: SEARCH_FIELD_LABELS[field],
      value: this.searchCriteria[field].trim(),
    }));
  }

  removeFilter(field: VoucherSearchField): void {
    this.searchCriteria[field] = '';
    this.search();
  }

  /** "/" focuses the search box; Escape clears the filters while focused in the bar. */
  @HostListener('document:keydown', ['$event'])
  onKeydown(event: KeyboardEvent): void {
    const target = event.target as HTMLElement;
    const isTyping = ['INPUT', 'TEXTAREA', 'SELECT'].includes(target.tagName) || target.isContentEditable;

    if (event.key === '/' && !isTyping) {
      event.preventDefault();
      this.searchInput?.nativeElement.focus();
    } else if (event.key === 'Escape' && target.closest('.voucher-filter') && this.hasActiveSearch()) {
      this.clearSearch();
      (target as HTMLElement).blur();
    }
  }

  delete(voucher: IVoucher): void {
    const modalRef = this.modalService.open(VoucherDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.voucher = voucher;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  // Persist a karat-stamp / delivery picture against the voucher (req 7 & 13).
  onPictureSelected(event: Event, voucher: IVoucher): void {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) {
      return;
    }
    const file = input.files[0];
    const reader = new FileReader();
    reader.onload = () => {
      const result = reader.result as string; // data:<mime>;base64,<data>
      const base64 = result.substring(result.indexOf(',') + 1);
      this.voucherService.partialUpdate({ id: voucher.id, hallmarkImage: base64, hallmarkImageContentType: file.type }).subscribe({
        next: () => {
          voucher.hallmarkImage = base64;
          voucher.hallmarkImageContentType = file.type;
          this.alertService.addAlert({ type: 'success', message: 'Picture uploaded.' });
        },
        error: () => this.alertService.addAlert({ type: 'danger', message: 'Picture upload failed.' }),
      });
    };
    reader.readAsDataURL(file);
  }

  load(): void {
    this.queryBackend().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(this.page, event);
  }

  navigateToPage(page: number): void {
    this.handleNavigation(page, this.sortState());
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page = +(page ?? 1);
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
    SEARCH_FIELDS.forEach(field => {
      this.searchCriteria[field] = params.get(SEARCH_FIELD_PARAM[field]) ?? '';
    });
    // Keep the advanced panel open when an advanced criterion arrives via the URL.
    if (ADVANCED_FIELDS.some(field => this.searchCriteria[field].trim() !== '')) {
      this.showAdvanced = true;
    }
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.vouchers.set(dataFromBody);
  }

  protected fillComponentAttributesFromResponseBody(data: IVoucher[] | null): IVoucher[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected queryBackend(): Observable<EntityArrayResponseType> {
    const { page } = this;

    this.isLoading = true;
    const pageToLoad: number = page;
    const queryObject: any = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    SEARCH_FIELDS.forEach(field => {
      const value = this.searchCriteria[field].trim();
      if (value !== '') {
        queryObject[SEARCH_FIELD_PARAM[field]] = value;
      }
    });
    return this.voucherService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(page: number, sortState: SortState): void {
    const queryParamsObj: any = {
      page,
      size: this.itemsPerPage,
      sort: this.sortService.buildSortParam(sortState),
    };

    SEARCH_FIELDS.forEach(field => {
      const value = this.searchCriteria[field].trim();
      queryParamsObj[SEARCH_FIELD_PARAM[field]] = value !== '' ? value : null;
    });

    this.ngZone.run(() => {
      this.router.navigate(['./'], {
        relativeTo: this.activatedRoute,
        queryParams: queryParamsObj,
      });
    });
  }
}
