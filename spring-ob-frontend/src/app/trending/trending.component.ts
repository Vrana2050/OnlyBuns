import { Component, OnInit } from '@angular/core';
import { PostService } from '../service/post.service';

@Component({
  selector: 'app-trending',
  templateUrl: './trending.component.html',
  styleUrls: ['./trending.component.css']
})
export class TrendingComponent implements OnInit {
  constructor(private postService: PostService) {}

  ngOnInit() {
    this.postService.countAllPosts().subscribe((response) => {
      this.allPostsCount = response;
      console.log(response);
    });

    this.postService.countThisMonthPosts().subscribe((response) => {
      this.allPostsThisMonthCount = response;
      console.log(response);
    });
  }

  allPostsCount: number = 0;
  allPostsThisMonthCount: number = 0;
}
