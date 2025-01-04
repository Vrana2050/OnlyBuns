import { Component, OnDestroy, OnInit, ViewChild, ElementRef } from '@angular/core';
import { Message } from '../model/message.model';
import { User } from '../model/user.model';
import { ChatService } from '../service/chat.service';
import { ActivatedRoute } from '@angular/router';
import { Chat } from '../model/chat.model';
import { UserService } from '../service/user.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit, OnDestroy {
  @ViewChild('messageContainer') private messageContainer!: ElementRef;
  
  messages: Message[] = [];
  currentChat?: Chat;
  newMessageContent: string = '';
  user: User | undefined;
  chatId: number = 0;
  private messageSubscription?: Subscription;

  constructor(
    private route: ActivatedRoute,
    private chatService: ChatService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    const userId = +this.route.snapshot.params['userId'];
    this.chatService.connect();

    this.userService.getMyInfo().subscribe(user => {
      this.user = user;
      
      if (this.user) {
        this.chatService.getOrCreateChat([this.user.id, userId]).subscribe(chat => {
          this.currentChat = chat;
          this.chatId = chat.id;

          // Load existing messages
          this.chatService.getMessages(this.chatId).subscribe(messages => {
            this.messages = messages.reverse(); // Reverse because they come in desc order
            this.scrollToBottom();
          });

          // Subscribe to new messages
          this.messageSubscription = this.chatService.subscribeToMessages(this.chatId)
            .subscribe(message => {
              console.log('New message received:', message);
              // Only add the message if it's not already in the array
              if (!this.messages.some(m => m.id === message.id)) {
                this.messages.push(message);
                this.scrollToBottom();
              }
            });
        });
      }
    });
  }

  sendMessage(): void {
    if (this.newMessageContent.trim() && this.user) {
      const message: Message = {
        content: this.newMessageContent,
        senderId: this.user.id,
        chatId: this.chatId,
        createdAt: new Date(),
        isRead: false
      };

      this.chatService.sendMessage(message);
      this.newMessageContent = '';
    }
  }

  private scrollToBottom(): void {
    setTimeout(() => {
      if (this.messageContainer) {
        const element = this.messageContainer.nativeElement;
        element.scrollTop = element.scrollHeight;
      }
    }, 100);
  }

  isMyMessage(message: Message): boolean {
    return message.senderId === this.user?.id;
  }

  ngOnDestroy(): void {
    if (this.messageSubscription) {
      this.messageSubscription.unsubscribe();
    }
    this.chatService.disconnect();
  }
}