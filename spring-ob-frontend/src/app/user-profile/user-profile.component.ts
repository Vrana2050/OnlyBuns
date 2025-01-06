import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { PostService } from '../service/post.service';
import { PostReadDto } from '../model/postRead.model';
import { User, PasswordChange } from '../model/user.model';
import { UserService } from '../service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {
  posts: PostReadDto[] = [];
  editingPostId: number | null = null;
  newDescription: string = '';
  user: User | null = null;

  passwordChange: PasswordChange = {
    oldPassword: '',
    newPassword: '',
    confirmPassword: ''
  };
  isChangePasswordModalOpen: boolean = false;
  passwordsMismatch: boolean = false;
  isPasswordValid: boolean = false;

  constructor(private postService: PostService, private userService: UserService, private router: Router) {}

  ngOnInit(): void {
    this.loadPosts();
    this.userService.getMyInfo().subscribe((response) => {
      this.user = response;
      console.log(response);
    });
  }

  validatePasswords(): void {
    this.passwordsMismatch = this.passwordChange.newPassword !== this.passwordChange.confirmPassword;
    this.isPasswordValid =
      this.passwordChange.newPassword.length >= 3 &&
      this.passwordChange.confirmPassword.length >= 3 &&
      !this.passwordsMismatch;
  }

  changePassword(): void {
    if (!this.isPasswordValid) {
      console.error('Password validation failed');
      return;
    }

    this.userService
  .changePassword({
    oldPassword: this.passwordChange.oldPassword,
    newPassword: this.passwordChange.newPassword
  })
  .subscribe(
    (response: any) => {
      console.log(response.message); // Handle the success message
        this.router.navigate(['/login']);
        alert('Password changed successfully. Please login again.');
    },
    (error) => {
      if (error.error && error.error.message) {
        console.error(error.error.message); // Display error message from backend
        alert(error.error.message);
      } else {
        console.error('An unexpected error occurred', error);
      }
    }
  );
  }

  openChangePasswordModal(): void {
    this.isChangePasswordModalOpen = true;
    console.log('Modal opened');
  }

  closeChangePasswordModal(): void {
    this.isChangePasswordModalOpen = false;
  }

  loadPosts() :void {
    this.postService.getAllPostsForUser().subscribe((response) => {
      this.posts = response;
    });
  }
  

  deletePost(post: PostReadDto): void {
    this.postService.deletePost(post.id).subscribe(
      (success) => {
        if (success) {
          
          console.log('Post deleted successfully');
          /*const index = this.posts.findIndex(p => p.id === post.id);
          this.posts.splice(index, 1);
          this.cdr.detectChanges();*/
          this.loadPosts();
        } else {
          console.error('Failed to delete post');
        }
      },
      (error) => {
        console.error('Error deleting post', error);
      }
    );
  }

  editPost(post: PostReadDto): void {
    this.editingPostId = post.id;
    this.newDescription = post.description;
  }

  saveDescription(post: PostReadDto): void {
    if (this.newDescription.trim() === '') {
      console.error('Description cannot be empty');
      return;
    }

    this.postService.editPostDescription(post.id, this.newDescription).subscribe(
      (updatedPost) => {
        console.log(updatedPost);
        const index = this.posts.findIndex(p => p.id === updatedPost.id);
        if (index !== -1) {
          this.posts[index].description = updatedPost.description;
        }
        this.editingPostId = null;
        console.log('Post description updated successfully');
      },
      (error) => {
        console.error('Error updating post description', error);
      }
    );
  }

  cancelEdit(): void {
    this.editingPostId = null;
  }
  selectedPost: any = null;

  openModal(post: any) {
    this.selectedPost = post;
  }

  closeModal() {
    this.selectedPost = null;
  }
}
