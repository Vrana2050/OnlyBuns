import {Injectable} from '@angular/core';
import {ApiService} from './api.service';
import {ConfigService} from './config.service';
import {map} from 'rxjs/operators';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { PostReadDto } from '../model/postRead.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  headers = new HttpHeaders({
    'Accept': 'application/json',
  });

  constructor(
    private apiService: ApiService,
    private config: ConfigService
  ) {
  }

  createPost(post : FormData) {
    return this.apiService.post('http://localhost:8082/api/posts', post,this.headers);
  }

  getAllPostsDescByDate() {
    return this.apiService.get('http://localhost:8082/api/posts/getAllSortedByTime');
  }

  countAllPosts() {
    return this.apiService.get('http://localhost:8082/api/posts/allTimePostCount');
  }

  countThisMonthPosts() {
    return this.apiService.get('http://localhost:8082/api/posts/thisMonthPostCount');
  }

  getAllPostsFollowing(){
    return this.apiService.post('http://localhost:8082/api/posts/following',{}).pipe(
      map((response: HttpResponse<PostReadDto[]>) => response.body as PostReadDto[])
    );
  }
  getAllPostsForUser() : Observable<PostReadDto[]>{
    return this.apiService.post('http://localhost:8082/api/posts/getPostsOfUser',{}).pipe(
      map((response: HttpResponse<PostReadDto[]>) => response.body as PostReadDto[])
    );
  }
  sendPostsToAds(postIds: number[]): Observable<any> {
    return this.apiService.post('http://localhost:8082/api/posts/sendAd', postIds).pipe(
      map((response: HttpResponse<any>) => response.body as any)
    );
  }
  deletePost(postId: number): Observable<boolean> {

    return this.apiService.post(`http://localhost:8082/api/posts/delete/${postId}`,{}).pipe(
      map((response: HttpResponse<boolean>) => response.body as boolean)
    );
  }
  editPostDescription(postId: number, newDescription: string): Observable<PostReadDto> {
    const body = { description: newDescription };
    return this.apiService.post(
      `http://localhost:8082/api/posts/edit/${postId}`,
      body
    ).pipe(
      map((response: HttpResponse<PostReadDto>) => response.body as PostReadDto)
    );
  }
  likePost(postId: number): Observable<PostReadDto> {
    return this.apiService.post(
      `http://localhost:8082/api/posts/like/${postId}`,
      {}
    ).pipe(
      map((response: HttpResponse<PostReadDto>) => response.body as PostReadDto)
    );
  }
  
}
