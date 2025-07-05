import { Component, OnInit } from '@angular/core';
import { PostService } from '../service/post.service';
import { PostReadDto } from '../model/postRead.model';
import { UserService } from '../service';
import { Router } from '@angular/router';
import { CommentService } from '../service/comment.service';
import { CommentCreate } from '../model/comment.model';
import { User } from '../model/user.model';
import { RoleName } from '../model/role.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  comment="";
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
  user : User | null = null;


  hasSignedIn() {
    return !!this.userService.isUserLoggedIn;
  }
  isAdmin(): boolean {
    return this.user?.roles?.some(role => role.name === 'ROLE_ADMIN') || false;
  }

  constructor(private postService: PostService,private commentService:CommentService, private userService : UserService,private router: Router) { }


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
    });
    this.userService.getMyInfo().subscribe((response) => {
      this.user = response;
      console.log(response);
    })
  }

  navigateToProfile(): void {
    this.router.navigate(['/profile']);
  }

  toggleLike(index: number) {
    const post = this.posts[index];
    this.postService.likePost(post.id).subscribe((response) => {
      this.posts[index] = response
    });
  }
  openLoginModal(): void {
    this.showLoginModal = true;
  }

  // Method to close login modal
  closeLoginModal(): void {
    this.showLoginModal = false;
  }
  AddComment(postId: number) {
    const comment:CommentCreate = {
      postId: postId,
      comment: this.comment
    }
 this.commentService.createComment(comment).subscribe((response) => {
  console.log(response);
        this.posts.forEach((post) => {
          if(post.id === postId){
            post.comments.unshift(response.body);
            console.log(post.comments);
          }
        }
      )});
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


