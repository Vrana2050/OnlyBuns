

// chat-list.component.ts
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ChatService } from '../service/chat.service';


interface ChatPreview {
  userId: string;
  username: string;
  lastMessage: string;
  timestamp: Date;
  unreadCount: number;
  avatarUrl: string;
}
@Component({
  selector: 'app-chat-list',
  templateUrl: './chat-list.component.html',
  styleUrls: ['./chat-list.component.css']
})
export class ChatListComponent implements OnInit {
  chats: ChatPreview[] = [];

  constructor(
    private router: Router,
    private chatService: ChatService
  ) {}

  ngOnInit() {
    this.loadChats();
  }

  loadChats() {
    // Replace with your API call
    this.chats = [
      {
        userId: '1',
        username: 'John Doe',
        lastMessage: 'Hey, how are you?',
        timestamp: new Date(),
        unreadCount: 2,
        avatarUrl: 'https://via.placeholder.com/40'
      }
    ];
  }

  openChat(userId: string) {
    this.router.navigate(['/chat', userId]);
  }
}