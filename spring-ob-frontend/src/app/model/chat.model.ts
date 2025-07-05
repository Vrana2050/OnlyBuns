import { User } from "./user.model";
import { UserReadDto } from "./userRead.model";

export interface Chat {
    id?: number;
    name: string;
    admin: UserReadDto;
    participants: UserReadDto[];
  }