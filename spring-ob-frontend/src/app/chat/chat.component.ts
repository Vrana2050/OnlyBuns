import { Component, OnDestroy, OnInit, ViewChild, ElementRef } from '@angular/core';
import { Message } from '../model/message.model';
import { User } from '../model/user.model';
import { ChatService } from '../service/chat.service';
import { ActivatedRoute } from '@angular/router';
import { Chat } from '../model/chat.model';
import { UserService } from '../service/user.service';
import { Subscription } from 'rxjs';
import { UserReadDto } from '../model/userRead.model';

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
  chatParticipants: UserReadDto[] = [];
  followers: UserReadDto[] = [];
  peopleToAdd: boolean = false;
  peopleToRemove: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private chatService: ChatService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    const chatId = +this.route.snapshot.params['chatId'];
    this.chatId = chatId;
    this.chatService.connect();


    this.userService.getMyInfo().subscribe(user => {
      this.user = user;
      
      if (chatId) {

        this.chatService.getChat(chatId).subscribe(chat => {
          this.currentChat = chat;

          this.loadParticipants();
          // Load existing messages


          this.chatService.getMessages(chatId).subscribe(messages => {
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
      }else{

      }
    });
  }


  sendMessage(): void {
    if (this.newMessageContent.trim() && this.user) {
      const message: Message = {
        content: this.newMessageContent,
        sender: this.user,
        chat: this.currentChat!,
        createdAt: new Date()
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
    return message.sender.id === this.user?.id;
  }

  ngOnDestroy(): void {
    if (this.messageSubscription) {
      this.messageSubscription.unsubscribe();
    }
    this.chatService.disconnect();
  }

  loadParticipants() {
    this.chatService.getChat(this.chatId).subscribe(chat => {
      this.chatParticipants = chat.participants.filter(member => member.id !== this.user?.id);
    });
  }

  checkAdminStatus() {
    var bool =  this.currentChat?.admin?.id === this.user?.id || this.currentChat?.admin === null;
    return bool;
  }

  add(id : number) {
    this.chatService.addMemberToChat(this.chatId, id).subscribe(() => {
          this.chatParticipants.push(this.followers.find(follower => follower.id === id)!);
          this.loadParticipants();
          this.loadFollowers();
        });
  } 
  

  openAddMemberDialog() {
    this.peopleToAdd = true;
    this.loadFollowers();
  }

  loadFollowers(): void {
    this.userService.getFollowers().subscribe(followers => {
      console.log('All Followers:', followers);
      const participantIds = new Set(this.chatParticipants.map(participant => participant.id));
      this.followers = followers.filter(follower => !participantIds.has(follower.id));
      console.log('Filtered Followers:', this.followers);
    });
  }
  
  

  openRemoveMemberDialog() {
    this.peopleToRemove = true;
  }

  closeAddMemberDialog() {
    this.peopleToAdd = false;
  }

  closeRemoveMemberDialog() {
    this.peopleToRemove = false;
  }
  

  remove(id : number) {
    this.chatService.removeMemberFromChat(this.chatId, id).subscribe(() => {
      this.loadParticipants();
    });
  }


}