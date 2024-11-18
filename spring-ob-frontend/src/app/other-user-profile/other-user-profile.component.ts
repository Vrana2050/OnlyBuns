import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../service';

@Component({
  selector: 'app-other-user-profile',
  templateUrl: './other-user-profile.component.html',
  styleUrls: ['./other-user-profile.component.css']
})
export class OtherUserProfileComponent implements OnInit {
  userId: number | undefined;
  user: any;
  isFollowing: boolean = false;

  constructor(private route: ActivatedRoute, private userService : UserService) {}

  ngOnInit(): void {
    // Subscribe to queryParams to retrieve the userId
    this.route.queryParams.subscribe(params => {
      this.userId = +params['userId'];
      console.log(this.userId);
    });

    this.userService.getById(this.userId).subscribe((response) => {
      this.user = response;
      console.log("OVO JE USER");
      console.log(response);
      this.checkFollowStatus();
    });
    
  }

  checkFollowStatus() {
    console.log("UBICU SE")
    console.log(this.user.id)
    this.userService.getFollowStatus(this.user.id).subscribe((response) => {
      console.log("DA LI PRATIM " + response);
      this.isFollowing = response;
    })
  }

  toggleFollow() {
    if(this.isFollowing){
      this.userService.unfollow(this.user.id).subscribe((response) => {
        this.isFollowing = !this.isFollowing;
        this.user.numberOfFollowers += this.isFollowing ? 1 : -1;
      })
    }else{
      this.userService.unfollow(this.user.id).subscribe((response) => {
        this.isFollowing = !this.isFollowing;
        this.user.numberOfFollowers += this.isFollowing ? 1 : -1;
      })

    }
  }
}
