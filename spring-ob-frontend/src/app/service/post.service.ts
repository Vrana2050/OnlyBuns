import {Injectable} from '@angular/core';
import {ApiService} from './api.service';
import {ConfigService} from './config.service';
import {catchError, map} from 'rxjs/operators';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PostReadDto, UserLikesDto } from '../model/postRead.model';

import { Observable, of } from 'rxjs';
import { AnalyticsDto } from '../model/analytics.model';


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

  getAllRabbitObjects() {
    return this.apiService.get('http://localhost:8082/api/rabbitCareObjects');
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


  getTop10AllTimePosts() : Observable<PostReadDto[]>{
    return this.apiService.get('http://localhost:8082/api/posts/getTop10AllTimePosts');
  }

  getTop5PostLastWeek() : Observable<PostReadDto[]>{
    return this.apiService.get('http://localhost:8082/api/posts/getTop5PostLastWeek');
  }

  getTop10LikersThisWeek() : Observable<UserLikesDto[]>{
    return this.apiService.get('http://localhost:8082/api/posts/findTop10UsersByLikesGivenThisWeek');
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
  
  getActivityCounts(startDate: Date, endDate: Date): Observable<{ posts: number, comments: number }> {
    const body = {
      startDate: startDate.toISOString(),
      endDate: endDate.toISOString(),
    };
  
    return this.apiService.post(
      'http://localhost:8082/api/analytics/activity',
      body
    ).pipe(
      map((response: HttpResponse<{ posts: number, comments: number }>) => 
        response.body as { posts: number, comments: number }
      )
    );
  }

  getUserActivityStats(): Observable<{ PostUsers: number, CommentUsers: number, InactiveUsers: number }> {
    return this.apiService.get('http://localhost:8082/api/analytics/user-activity-stats').pipe(
      map((response) => {
        // Log the response for debugging
        console.log('Response from API:', response);
  
        // Ensure that the response is defined and return it or fallback if undefined
        return response || { PostUsers: 0, CommentUsers: 0, InactiveUsers: 0 };  // Fallback data
      }),
      catchError((error) => {
        console.error('Error fetching stats:', error);
        return of({ PostUsers: 0, CommentUsers: 0, InactiveUsers: 0 });  // Fallback data
      })
    );
  }
  
  
  
  
  
  
  
}
