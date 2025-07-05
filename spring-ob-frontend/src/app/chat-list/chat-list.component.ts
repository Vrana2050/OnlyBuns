
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ChatService } from '../service/chat.service';
import { ChatInbox } from '../model/chatInbox.model';
import { UserService } from '../service/user.service';
import { User } from '../model/user.model';
import { UserReadDto } from '../model/userRead.model';


@Component({
  selector: 'app-chat-list',
  templateUrl: './chat-list.component.html',
  styleUrls: ['./chat-list.component.css']
})
export class ChatListComponent implements OnInit,OnDestroy {
  chats: ChatInbox[] = [];
  user : User | undefined;
  openSelectParticipants = false;
  followers: UserReadDto[] = [];
  participantsIdsForNewChat: number[] = [];

  constructor(
    private router: Router,
    private chatService: ChatService,
    private userService: UserService
  ) {}

  ngOnInit() {
    console.log('ChatListComponent initializing...');
    this.userService.getMyInfo().subscribe({
      next: (user) => {
        console.log('Received user info:', user);
        this.user = user;
        this.listenForNewChats();  // Only set up listener after we have user info
        this.loadChats();
      },
      error: (error) => {
        console.error('Error getting user info:', error);
      }
    });
}

loadChats() {
  this.chatService.getChatsForUser().subscribe((allChats) => {
    console.log('Received chats from API:', allChats);
    this.chats = allChats
      .filter(chat => chat.participants.some(participant => participant.id === this.user?.id))
      .map(chat => {
        return {
          ...chat,
          participants: chat.participants.filter(participant => participant.id !== this.user?.id)
        };
      });
    console.log('Filtered chats:', this.chats);
  });
}

  

  openChat(chatId: number | undefined) {
    if (chatId !== undefined) {
      this.router.navigate(['/chat', chatId]);
    }
  }

  createANewChat() {
    this.openSelectParticipants = true;
    this.loadFollowers();
  }

  closeSelectParticipantsDialog() {
    this.openSelectParticipants = false;
  }

  loadFollowers(): void {
    this.userService.getFollowers().subscribe(followers => {
      console.log(followers);
      this.followers = followers;
    });
  }

  toggleParticipantSelection(participantId: number) {
    if (this.participantsIdsForNewChat.includes(participantId)) {
      this.participantsIdsForNewChat = this.participantsIdsForNewChat.filter(id => id !== participantId);
    } else {
      this.participantsIdsForNewChat.push(participantId);
    }
  }

  makeAChatWithParticipants() {
    this.participantsIdsForNewChat.push(this.user?.id!);
    this.chatService.createChat(this.participantsIdsForNewChat).subscribe(() => {
      this.loadChats();
      this.openSelectParticipants = false;
      this.participantsIdsForNewChat = [];
    });
  }

  listenForNewChats() {
    if (this.user) {
      console.log('Setting up WebSocket subscription for user:', this.user.id);
  
      this.chatService.connect().then(() => {
        console.log('WebSocket connected, subscribing to new chats...');
  
        this.chatService.subscribeToNewChats(this.user?.id!).subscribe((newChat) => {
          console.log('New chat received:', newChat);
  
          // Check if a chat with the exact same participants already exists
          const isDuplicate = this.chats.some(chat => 
            chat.participants.length === newChat.participants.length &&
            chat.participants.every((p: any) => newChat.participants.some((np: any) => np.id === p.id))
          );
  
          if (!isDuplicate) {
            console.log('Adding new chat:', newChat);
            this.chats.unshift(newChat);
          } else {
            console.log('Chat with same participants already exists, ignoring:', newChat);
          }
        });
      }).catch((error) => {
        console.error('Failed to connect to WebSocket:', error);
      });
    }
  }
  
  
  
  
  

  

  ngOnDestroy() {
    this.chatService.disconnect();
  }
  
  
}