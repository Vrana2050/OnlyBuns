import { User } from "./user.model";

export interface Chat {
    id: number;
    name: string;
    isGroupChat: boolean;
    admin?: User;
    participants: User[];
  }