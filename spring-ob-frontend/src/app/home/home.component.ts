import { Component, OnInit } from '@angular/core';
import {FooService} from '../service/foo.service';
import {UserService} from '../service/user.service';
import {ConfigService} from '../service/config.service';
import {Router} from '@angular/router';
import { PostService } from '../service/post.service';
import { PostReadDto } from '../model/postRead.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  isPopupVisible: boolean = false;

  posts : PostReadDto[] = [];

  constructor(private postService : PostService) { }

  ngOnInit() {
    this.postService.getAllPostsDescByDate().subscribe((response) => {
      this.posts = response;
      console.log(response);
    });


   }

  openPopup(): void {
    this.isPopupVisible = true;
  }

  closePopup(): void {
    this.isPopupVisible = false; 
  }
}