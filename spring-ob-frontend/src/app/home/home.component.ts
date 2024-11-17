import { Component, OnInit } from '@angular/core';
import { PostService } from '../service/post.service';
import { PostReadDto } from '../model/postRead.model';
import { UserService } from '../service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  fooResponse = {};
  whoamIResponse = {};
  allUserResponse = {};
  isPopupVisible: boolean = false;
  posts: PostReadDto[] = [
    {
      id: 0,
      description: "",
      postDate: "",
      creator: {
        id: 0,
        username: ""
      },
      likes: 0,
      numOfComments: 0,
      imageBase64: "",
      comments: []
    }
  ];
  
  
  postsNotSignedIn : PostReadDto[] = [];
  selectedPostIndex: number | null = null;
  showLoginModal: boolean = false;

  hasSignedIn() {
    return !!this.userService.isUserLoggedIn;
  }


  constructor(private postService: PostService, private userService : UserService,private router: Router) { }


  ngOnInit() {
    this.userService.getMyInfo().subscribe((response) => {
      console.log(response);
    });

    this.postService.getAllPostsDescByDate().subscribe((response) => {
      this.postsNotSignedIn = response;
      console.log(response);
    });

    this.postService.getAllPostsFollowing().subscribe((response) => {
      this.posts = response;
      console.log("KUREEEEEEEEE");
      console.log(this.posts[0].comments);
      console.log("KUREEEEEEEEE" + this.hasSignedIn());
      console.log(response)
    });
  }

  navigateToProfile(): void {
    this.router.navigate(['/profile']);
  }

  toggleLike(index: number) {
    const post = this.posts[index];
    /*
    post.likedByCurrentUser = !post.likes;
  
    if (post.likedByCurrentUser) {
      post.likes += 1; // Increment likes
      this.postService.likePost(post.id).subscribe();
    } else {
      post.likes -= 1; // Decrement likes
      this.postService.unlikePost(post.id).subscribe();
    }*/
  }
  openLoginModal(): void {
    this.showLoginModal = true;
  }

  // Method to close login modal
  closeLoginModal(): void {
    this.showLoginModal = false;
  }


  lookAtProfile(userId: number) {
    this.router.navigate(['/other-user-profile'], { queryParams: { userId: userId } });
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


