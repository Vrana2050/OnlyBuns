import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../service';

@Component({
  selector: 'app-other-user-profile',
  templateUrl: './other-user-profile.component.html',
  styleUrls: ['./other-user-profile.component.css'],
})
export class OtherUserProfileComponent implements OnInit {
  userId!: number;
  user: any;
  isFollowing: boolean = false;
  currentUserId!: number;

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      this.userId = +params['userId'];

      // Dobavljamo korisnika profila koji gledamo
      this.userService.getById(this.userId).subscribe((response) => {
        this.user = response;

        // Kada dobijemo korisnika, proveravamo status praćenja
        this.checkFollowStatus();
      });
    });

    // Dobavljamo info o trenutno prijavljenom korisniku
    this.userService.getMyInfo().subscribe((me) => {
      this.currentUserId = me.id;
    });
  }

  checkFollowStatus() {
    this.userService.getFollowStatus(this.userId).subscribe((response) => {
      this.isFollowing = response;
    });
  }

  toggleFollow() {
    if (this.isFollowing) {
      this.userService.unfollow(this.userId).subscribe(() => {
        this.isFollowing = false;
        this.user.numberOfFollowers--;
      });
    } else {
      this.userService.follow(this.userId).subscribe(() => {
        this.isFollowing = true;
        this.user.numberOfFollowers++;
      });
    }
  }
}
