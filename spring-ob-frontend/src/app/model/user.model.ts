import { Role } from './role.model';

export interface User {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    numberOfPosts: number;
    numberOfFollowing: number;
    roles: Role[];
  }
  