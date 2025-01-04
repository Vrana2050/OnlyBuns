import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { Client } from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';
import { Message } from '../model/message.model';
import { Chat } from '../model/chat.model';

@Injectable({
  providedIn: 'root',
})
export class ChatService {
  private baseUrl = 'http://localhost:8082/api/chats';
  private stompClient: Client;
  private messageSubjects = new Map<number, Subject<Message>>();

  constructor(private http: HttpClient) {
    this.stompClient = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8082/ws'),
      onConnect: () => {
        console.log('Connected to WebSocket');
      },
      onDisconnect: () => {
        console.log('Disconnected from WebSocket');
      }
    });
  }

  connect(): void {
    if (!this.stompClient.active) {
      this.stompClient.activate();
    }
  }

  subscribeToMessages(chatId: number): Observable<Message> {
    if (!this.messageSubjects.has(chatId)) {
      const subject = new Subject<Message>();
      this.messageSubjects.set(chatId, subject);

      // Subscribe to the specific chat topic
      this.stompClient.subscribe(`/topic/chat/${chatId}`, (message) => {
        const receivedMessage = JSON.parse(message.body) as Message;
        console.log('Received message:', receivedMessage);
        subject.next(receivedMessage);
      });
    }

    return this.messageSubjects.get(chatId)!.asObservable();
  }

  sendMessage(message: Message): void {
    if (this.stompClient.active) {
      this.stompClient.publish({
        destination: '/app/send',
        body: JSON.stringify(message)
      });
    } else {
      console.error('STOMP client is not connected');
    }
  }

  getMessages(chatId: number): Observable<Message[]> {
    return this.http.get<Message[]>(`${this.baseUrl}/${chatId}/messages`);
  }

  getOrCreateChat(userIds: number[]): Observable<Chat> {
    return this.http.post<Chat>(`${this.baseUrl}/create-or-get`, userIds);
  }

  disconnect(): void {
    this.messageSubjects.forEach(subject => subject.complete());
    this.messageSubjects.clear();
    if (this.stompClient.active) {
      this.stompClient.deactivate();
    }
  }
}