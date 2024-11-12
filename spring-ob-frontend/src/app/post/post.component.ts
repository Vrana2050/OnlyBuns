import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PostService } from '../service/post.service';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent {
  coordinates: { latitude: number, longitude: number } | undefined;

  postForm: FormGroup;
  selectedFile: File | null = null;

  constructor(private fb: FormBuilder,private postService: PostService) {
    this.postForm = this.fb.group({
      description: ['', Validators.required], // Ispravljen red
      latitude: [null],
      longitude: [null],
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
    if(this.postForm.get('latitude')?.value == null || this.postForm.get('longitude')?.value == null){
      alert('Please select location!');
      return;
    }
    if(this.postForm.invalid){
      alert('Please fill form!');
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

    this.postService.createPost(formData).subscribe(
      (response :any) => {
        alert('Post created successfully!'+ response);
      },
      (error : any) => {
        alert('Error creating post!' + error);
      }
    );
  }

  getCords(coordinates: { latitude: number, longitude: number }) {
    this.postForm.patchValue({
      latitude: coordinates.latitude,
      longitude: coordinates.longitude,
    });
    console.log('Received coordinates:', this.postForm.get('latitude')?.value, this.postForm.get('longitude')?.value);
  }
}
