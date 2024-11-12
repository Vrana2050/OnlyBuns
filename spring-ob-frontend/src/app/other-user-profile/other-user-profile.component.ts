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

  constructor(private route: ActivatedRoute, private userService : UserService) {}

  ngOnInit(): void {
    // Subscribe to queryParams to retrieve the userId
    this.route.queryParams.subscribe(params => {
      this.userId = +params['userId'];
      console.log(this.userId);
    });

    this.userService.getById(this.userId).subscribe((response) => {
      this.user = response;
      console.log(response);
    }
    );
  }
}
