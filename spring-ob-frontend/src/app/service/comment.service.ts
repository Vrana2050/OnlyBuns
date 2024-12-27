import {Injectable} from '@angular/core';
import {ApiService} from './api.service';
import {ConfigService} from './config.service';
import {map} from 'rxjs/operators';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { PostReadDto } from '../model/postRead.model';
import { Observable } from 'rxjs';
import { CommentCreate } from '../model/comment.model';
@Injectable({
  providedIn: 'root'
})
export class CommentService {

  headers = new HttpHeaders({
    'Accept': 'application/json',
  });

  constructor(
    private apiService: ApiService,
    private config: ConfigService
  ) {
  }

  createComment(comment : CommentCreate) {
    return this.apiService.post('http://localhost:8082/api/comment', comment);
  }

  
  
}