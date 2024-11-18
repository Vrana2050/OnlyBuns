import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { PostService } from '../service/post.service';
import { PostReadDto } from '../model/postRead.model';
import { User } from '../model/user.model';
import { UserService } from '../service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit{
  posts: PostReadDto[] = [];
  editingPostId: number | null = null;
  newDescription: string = '';
  user: User | null = null;
  
  constructor(private postService : PostService,private userService : UserService){}


  ngOnInit(): void {
    this.loadPosts();
    this.userService.getMyInfo().subscribe((response) => {
      this.user = response;
      console.log(response);
    })

    
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
