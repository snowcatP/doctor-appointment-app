import { Client, Stomp } from '@stomp/stompjs';
import { Injectable } from '@angular/core';
import { host } from '../../../environments/environment';
import { BehaviorSubject, Observable } from 'rxjs';
import SockJS from 'sockjs-client/dist/sockjs';

@Injectable({
  providedIn: 'root',
})
export class WebSocketService {
  private client: Client;
  private state$: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  constructor() {
    let ws = new SockJS(`${host}/ws`);
    this.client = Stomp.over(ws);
    // this.client.debug = (msg: string) => console.log(msg);
    this.client.debug = () => {};
    this.client.configure({
      onConnect: () => {
        console.log('Connected to WebSocket');
        this.state$.next(true);
      },
      onDisconnect: () => {
        console.log('Disconnected from WebSocket');
        // this.state$.next(false);
      },
      onStompError: (frame) => {
        console.error('STOMP error:', frame);
      },
    });
  }

  emit(destination: string, data: any) {
    if (this.state$.value) {
      this.client.publish({
        destination,
        body: JSON.stringify(data),
      });
    } else {
      console.error('WebSocket connection not established');
    }
  }

  on(event: string): Observable<any> {
    return new Observable((observer) => {
      const subscription = this.client.subscribe(event, (message) => {
        try {
          const parsedBody = JSON.parse(message.body);
          observer.next(parsedBody);
        } catch (error) {
          observer.error(error);
        }
      });
      return () => {
        if (subscription) {
          subscription.unsubscribe();
        }
      };
    });
  }

  connectSocket(): Observable<boolean> {
    if (!this.client.connected) {
    }
    this.client.activate();
    return this.state$.asObservable();
  }

  disconnectSocket() {
    if (this.client && this.client.connected) {
      this.client.deactivate();
      this.state$.next(false);
      this.state$.complete();
    }
  }
}
