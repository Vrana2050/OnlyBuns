import { LocationCreate } from './location';

export interface PostCreate{
    description: string;
    location: LocationCreate;
    tags: string[];
}