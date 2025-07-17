import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommentCreate } from '../model/comment.model';
import { PostReadDto } from '../model/postRead.model';
import { User } from '../model/user.model';
import { UserService } from '../service';
import { CommentService } from '../service/comment.service';
import { PostService } from '../service/post.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  comments: { [key: number]: string } = {};
  isFollowing: boolean = false;
  postIdsToAds: number[] = [];
  fooResponse = {};
  whoamIResponse = {};
  hasPostsToPromote = false;
  allUserResponse = {};
  isUser = false;
  isAdmin = false;
  isPopupVisible: boolean = false;
  posts: PostReadDto[] = [
    {
      id: 0,
      description: '',
      postDate: '',
      creator: {
        id: 0,
        username: '',
      },
      likes: 0,
      numOfComments: 0,
      imageBase64: '',
      comments: [],
    },
  ];

  postsNotSignedIn: PostReadDto[] = [];
  selectedPostIndex: number | null = null;
  showLoginModal: boolean = false;
  user: User | null = null;

  hasSignedIn() {
    return !!this.userService.isUserLoggedIn;
  }
  /*isAdmin(): boolean {
  console.log("isAdmin: " + (this.user?.roles?.some(role => role.name === 'ROLE_ADMIN') || false));
    return this.user?.roles?.some(role => role.name === 'ROLE_ADMIN') || false;
  }*/

  constructor(
    private postService: PostService,
    private commentService: CommentService,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit() {
    this.userService.getMyInfo().subscribe((response) => {
      this.user = response;
    });

    this.postService.getAllPostsDescByDate().subscribe((response) => {
      this.postsNotSignedIn = response;
    });

    this.postService.getAllPostsFollowing().subscribe((response) => {
      this.posts = response;
    });

    this.getRole();
  }
  getRole() {
    this.userService.getMyInfo().subscribe((response) => {
      this.isAdmin = response.roles.some(
        (role: any) => role.name === 'ROLE_ADMIN'
      );
      this.isAdmin = response.roles.some(
        (role: any) => role.name === 'ROLE_ADMIN'
      );
      this.isUser = response.roles.some(
        (role: any) => role.name === 'ROLE_USER'
      );
      if (this.isAdmin) {
        console.log('Admin');
        this.postService.getAllPostsDescByDate().subscribe((response) => {
          this.posts = response;
          console.log(response);
        });
      }
      if (this.isUser) {
        console.log('User');
      }
    });
  }
  sendPostsToAds() {
    this.postService.sendPostsToAds(this.postIdsToAds).subscribe((response) => {
      console.log(response);
      this.postIdsToAds = [];
    });
  }
  navigateToProfile(): void {
    this.router.navigate(['/profile']);
  }
  sendToAds(id: number, event: any): void {
    const isChecked = event.target.checked;
    if (isChecked) {
      this.postIdsToAds.push(id);
      this.hasPostsToPromote = true;
    } else {
      this.postIdsToAds = this.postIdsToAds.filter((postId) => postId !== id);
      this.hasPostsToPromote = this.postIdsToAds.length > 0;
    }
  }
  toggleLike(index: number) {
    const post = this.posts[index];
    this.postService.likePost(post.id).subscribe((response) => {
      this.posts[index] = response;
    });
  }
  openLoginModal(): void {
    console.log('Opening login modal');
    this.showLoginModal = true;
  }

  // Method to close login modal
  closeLoginModal(): void {
    this.showLoginModal = false;
  }
  AddComment(postId: number) {
    const text = this.comments[postId];
    if (!text?.trim()) return;

    const comment: CommentCreate = {
      postId: postId,
      comment: text,
    };

    this.commentService.createComment(comment).subscribe((response) => {
      const created = response.body;
      this.posts.forEach((post) => {
        if (post.id === postId) {
          post.comments.unshift(created);
        }
      });
      this.comments[postId] = ''; // Clear input
    });
  }

  lookAtProfile(userId: number) {
    this.router.navigate(['/other-user-profile'], {
      queryParams: { userId: userId },
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

  checkFollowStatus() {
    if (!this.user) return;

    this.userService.getFollowStatus(this.user.id).subscribe((response) => {
      console.log('DA LI PRATIM ' + response);
      this.isFollowing = response;
    });
  }
}
