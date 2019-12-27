import { Component, OnInit, OnDestroy } from '@angular/core';
import { MessageService } from 'app/entities/messaging/services/messaging.service';

@Component({
  selector: 'jhi-aurum-messaging',
  templateUrl: './messaging.component.html',
  styleUrls: ['./messaging.component.scss']
})
export class MessagingComponent implements OnInit, OnDestroy {
  constructor(private messageService: MessageService) {}

  ngOnInit() {}

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
}
