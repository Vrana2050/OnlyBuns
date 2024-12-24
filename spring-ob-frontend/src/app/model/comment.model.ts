import { UserReadDto } from "./userRead.model";

export interface CommentReadDto{
    id: number;
    text: string;
    creator: UserReadDto;
    created: string;
}
export interface CommentCreate{
    postId: number,
    comment: string
}