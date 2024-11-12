import {Injectable} from '@angular/core';
import {ApiService} from './api.service';
import {ConfigService} from './config.service';
import {catchError, map} from 'rxjs/operators';
import { Observable, throwError } from 'rxjs';
import { User } from '../model/user.model';
import { HttpParams, HttpResponse } from '@angular/common/http';
import { Page } from '../model/page.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  currentUser!:any;
  isUserLoggedIn: boolean = false;

  constructor(
    private apiService: ApiService,
    private config: ConfigService
  ) {
  }

  getMyInfo() {
    return this.apiService.get(this.config.whoami_url)
      .pipe(map(user => {
        this.currentUser = user;
        this.isUserLoggedIn = true;
        return user;
      }));
      
  }

  getAll() {
    return this.apiService.get(this.config.users_url);
  }

  getFilteredUsers(filter: any): Observable<Page<User>> {
    return this.apiService.post('http://localhost:8082/api/user/allUsersFiltered', filter).pipe(
      map((response: HttpResponse<Page<User>>) => response.body as Page<User>)
    );
  }
  
  
  getUsers(page: number, pageSize: number, filters: any, sortBy: string, sortDirection: string): Observable<any> {
    const requestBody = {
      page: page,
      size: pageSize,
      sortBy: sortBy,
      sortDirection: sortDirection,
      firstName: filters.firstName,
      lastName: filters.lastName,
      email: filters.email,
      minPosts: filters.minPosts,
      maxPosts: filters.maxPosts
    };

    console.log('Request Body:', requestBody);  // Log request body

    return this.apiService.post('http://localhost:8082/api/user/allUsers', requestBody)
      .pipe(
        catchError(error => {
          console.error('Error fetching users:', error);
          return throwError(error);  // Handle error appropriately
        })
      );
}
  

}
