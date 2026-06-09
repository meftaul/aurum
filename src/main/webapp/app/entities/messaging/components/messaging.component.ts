import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { MessageService } from 'app/entities/messaging/services/messaging.service';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { ICustomer } from 'app/entities/customer/customer.model';
import { AlertService } from 'app/core/util/alert.service';

@Component({
  selector: 'jhi-aurum-messaging',
  templateUrl: './messaging.component.html',
  styleUrls: ['./messaging.component.scss']
})
export class MessagingComponent implements OnInit, OnDestroy {
  customers: ICustomer[];
  eventSubscriber: Subscription;
  sendList: any[] = [];

  constructor(
    private messageService: MessageService,
    private customerService: CustomerService,
    protected jhiAlertService: AlertService
  ) {}

  ngOnInit() {
    this.fetchCustomer();
  }

  ngOnDestroy() {}

  sendMessage() {
    // eslint-disable-next-line no-console
    console.info('sending message');
    this.messageService.postMessage('01723466685', 'Hello from MaitreeGold').subscribe(
      data => {
        // eslint-disable-next-line no-console
        console.log(data);
      },
      error => {
        console.error(error);
      }
    );
  }

  fetchCustomer() {
    this.customerService
      .query({})
      .subscribe((res: HttpResponse<ICustomer[]>) => (this.customers = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  addToSendList(customer: ICustomer) {
    if (this.sendList.filter(data => data === customer.phone).length > 0) {
      alert('This number is already added.');
      return;
    }
    this.sendList.push(customer.phone);
  }

  removeFromSendList(index) {
    // eslint-disable-next-line no-console
    // console.log(index);
    this.sendList.splice(index, 1);
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.addAlert({ type: 'danger', message: errorMessage });
  }
}
