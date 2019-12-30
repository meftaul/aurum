import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MessageModel } from 'app/entities/messaging/model/message.model';

@Injectable({ providedIn: 'root' })
export class MessageService {
  public MESSAGE_API_URL = 'http://66.45.237.70/api.php';

  constructor(protected http: HttpClient) {}

  postMessage(listOfNumbers: any, messageText: string): Observable<any> {
    const message = new MessageModel();
    message.username = '01977051020';
    message.password = '6fv92py8';
    message.message = messageText;
    message.number = listOfNumbers;

    return this.http.post(this.MESSAGE_API_URL, message);
  }
}
