import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { PostService } from '../service/post.service';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent {
  postForm: FormGroup;
  selectedFile: File | null = null;

  constructor(private fb: FormBuilder,private postService: PostService) {
    this.postForm = this.fb.group({
      description: [''],
      latitude: 0,
      longitude: 0,
    });
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
    }
  }

  onSubmit(): void {
    if (!this.selectedFile) {
      alert('Please select an image file!');
      return;
    }

    const formData = new FormData();

    // Kreiranje JSON objekta iz Reactive Form-a
    const postDto = {
      description: this.postForm.get('description')?.value,
      location: {
        longitude: this.postForm.get('longitude')?.value,
        latitude: this.postForm.get('latitude')?.value,
      },
    };

    // Dodavanje JSON i slike u FormData
    formData.append('postDto', new Blob([JSON.stringify(postDto)], { type: 'application/json' }));
    formData.append('image', this.selectedFile);
    console.log(postDto);
    console.log(this.selectedFile)
    console.log(formData)

    this.postService.createPost(formData).subscribe(
      (response :any) => {
        alert('Post created successfully!'+ response);
      },
      (error : any) => {
        alert('Error creating post!' + error);
      }
    );
  }

}
