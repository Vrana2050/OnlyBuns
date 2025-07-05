import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { Client } from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';
import { Message } from '../model/message.model';
import { Chat } from '../model/chat.model';
import { ChatInbox } from '../model/chatInbox.model';

@Injectable({
  providedIn: 'root',
})
export class ChatService {
  private baseUrl = 'http://localhost:8082/api/chats';
  private stompClient: Client;
  private messageSubjects = new Map<number, Subject<Message>>();
  private newChatSubject = new Subject<ChatInbox>();


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
  connect(): Promise<void> {
    return new Promise((resolve, reject) => {
      if (this.stompClient.active) {
        console.log('WebSocket already connected');
        resolve();
        return;
      }
  
      this.stompClient.onConnect = () => {
        console.log('WebSocket connected');
        resolve();
      };
  
      this.stompClient.onStompError = (error) => {
        console.error('WebSocket connection error:', error);
        reject(error);
      };
  
      this.stompClient.activate();
    });
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

  subscribeToNewChats(userId: number): Observable<ChatInbox> {
    if (!this.stompClient.active) {
      throw new Error('WebSocket connection is not active');
    }
  
    const subject = new Subject<ChatInbox>();
  
    this.stompClient.subscribe(`/topic/new-chat/${userId}`, (message) => {
      const newChat = JSON.parse(message.body) as ChatInbox;
      console.log('Received new chat:', newChat);
  
      // Remove current user from participants
      newChat.participants = newChat.participants.filter(p => p.id !== userId);
      
      console.log('Filtered new chat:', newChat);
      subject.next(newChat);
    });
  
    return subject.asObservable();
  }
  
  
  

  
  

  getMessages(chatId: number): Observable<Message[]> {
    return this.http.get<Message[]>(`${this.baseUrl}/${chatId}/messages`, {});
  }

  getChat(chatId: number): Observable<Chat> {
    return this.http.get<Chat>(`${this.baseUrl}/${chatId}`);
  }

  getOrCreateChat(userIds: number[]): Observable<Chat> {
    return this.http.post<Chat>(`${this.baseUrl}/create-or-get`, userIds);
  }

  addMemberToChat(chatId: number, id: number) {
    return this.http.post(`${this.baseUrl}/${chatId}/members/${id}`, {});
  }

  removeMemberFromChat(chatId: number, id: number) {
    return this.http.delete(`${this.baseUrl}/${chatId}/members/${id}`, {});
  }

  createChat(userIds: number[]): Observable<Chat> {
    return this.http.post<Chat>(`${this.baseUrl}/newChat`, userIds);
  }

  getChatsForUser() : Observable<ChatInbox[]> {
    return this.http.get<ChatInbox[]>(`${this.baseUrl}/forUser`,{});
  }

  disconnect(): void {
    this.messageSubjects.forEach(subject => subject.complete());
    this.messageSubjects.clear();
    if (this.stompClient.active) {
      this.stompClient.deactivate();
    }
  }
}