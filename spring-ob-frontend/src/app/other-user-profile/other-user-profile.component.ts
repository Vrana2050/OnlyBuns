import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
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

  constructor(private route: ActivatedRoute, private userService : UserService,private router: Router,) {}

  ngOnInit(): void {
    // Subscribe to queryParams to retrieve the userId
    this.route.queryParams.subscribe(params => {
      this.userId = +params['userId'];
    });

    this.userService.getById(this.userId).subscribe((response) => {
      this.user = response;
      this.checkFollowStatus();
    });
    
  }

  checkFollowStatus() {
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

  startChat() {
    if (this.user && this.user.id) {
      this.router.navigate(['/chat', this.user.id]); // Pass userId in the route
    }
  }
  
}
