
import { Message } from "./message.model";
import { UserReadDto } from "./userRead.model";

export interface ChatInbox {
    chatId?: number;
    participants: UserReadDto[];
    lastMessage?: Message;
  }