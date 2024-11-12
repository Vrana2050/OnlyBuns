import {Injectable} from '@angular/core';
import {ApiService} from './api.service';
import {ConfigService} from './config.service';
import {map} from 'rxjs/operators';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { PostReadDto } from '../model/postRead.model';

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

}
