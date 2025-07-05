import { Role } from './role.model';

export interface User {
    id: number;
    username: string;
    firstName: string;
    lastName: string;
    email: string;
    numberOfPosts: number;
    numberOfFollowing: number;
    numberOfFollowers: number;
    roles: Role[];
    address: string;
  }

export interface PasswordChange {
    oldPassword: string;
    newPassword: string;
    confirmPassword: string;
}
  