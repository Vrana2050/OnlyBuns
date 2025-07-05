import { Component, OnInit } from '@angular/core';
import { PostService } from '../service/post.service';
import { PostReadDto, UserLikesDto } from '../model/postRead.model';

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

    this.postService.getTop10AllTimePosts().subscribe((posts) => {
      this.top10AllTimePosts = posts;
    });

    this.postService.getTop5PostLastWeek().subscribe((posts) => {
      this.top5PostsLastWeek = posts;
    });

    this.postService.getTop10LikersThisWeek().subscribe((users) => {
      console.log(users);
      this.top10Likers = users;
    });
  }

  allPostsCount: number = 0;
  allPostsThisMonthCount: number = 0;
  top10AllTimePosts: PostReadDto[] = [];
  top5PostsLastWeek: PostReadDto[] = [];
  top10Likers: UserLikesDto[] = [];
}
