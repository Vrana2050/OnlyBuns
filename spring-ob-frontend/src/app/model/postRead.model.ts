import { CommentReadDto } from "./comment.model";
import { UserReadDto } from "./userRead.model";
import { User } from "./user.model";

export interface PostReadDto{
    id: number;
    description: string;
    postDate: string;
    creator: UserReadDto;
    likes: number;
    numOfComments: number;
    imageBase64: string;
    comments: CommentReadDto[];
    location?: LocationDto;
}

export interface RabbitCareObject{
    id: number;
    name: string;
    longitude: number;
    latitude: number;
}

export interface LocationDto{
    id: number;
    latitude: number;
    longitude: number;
}

export interface UserLikesDto{
    user: User;
    likeCount: number;
}