import { Component, OnInit } from '@angular/core';
import { PostService } from '../service/post.service';
import { PostReadDto } from '../model/postRead.model';
import { UserService } from '../service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  isPopupVisible: boolean = false;
  posts: PostReadDto[] = [];
  selectedPostIndex: number | null = null;

  hasSignedIn() {
    return !!this.userService.currentUser;
  }

  constructor(private postService: PostService, private userService : UserService) { }

  ngOnInit() {
    
    this.postService.getAllPostsDescByDate().subscribe((response) => {
      this.posts = response;
      console.log(response);
    });
  }

  openPopup(index: number): void {
    this.selectedPostIndex = index;
    this.isPopupVisible = true;
  }

  closePopup(): void {
    this.isPopupVisible = false;
    this.selectedPostIndex = null;
  }
}
