import { Chat } from "./chat.model";
import { User } from "./user.model";

export interface Message {
  id?: number;
  content: string;
  senderId: number;
  chatId: number;
  createdAt: Date;
  isRead: boolean;
  }