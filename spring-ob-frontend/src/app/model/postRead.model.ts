import { CommentReadDto } from "./comment.model";
import { UserReadDto } from "./userRead.model";

export interface PostReadDto{
    id: number;
    description: string;
    postDate: string;
    creator: UserReadDto;
    likes: number;
    numOfComments: number;
    imageBase64: string;
    comments: CommentReadDto[];
}