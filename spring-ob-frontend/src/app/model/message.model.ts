import { Chat } from "./chat.model";
import { User } from "./user.model";
import { UserReadDto } from "./userRead.model";

export interface Message {
  id?: number;
  content: string;
  sender: UserReadDto;
  chat: Chat;
  createdAt: Date;
  }