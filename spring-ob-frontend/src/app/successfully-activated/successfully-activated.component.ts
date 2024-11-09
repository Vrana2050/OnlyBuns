import { Component, OnInit } from '@angular/core';
import { AuthService } from '../service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-successfully-activated',
  templateUrl: './successfully-activated.component.html',
  styleUrls: ['./successfully-activated.component.css']
})
export class SuccessfullyActivatedComponent implements OnInit {

  userId: number = -1;

  constructor(
    private route: ActivatedRoute,
    private authService: AuthService,
  ) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.userId = params['userId'];
    });

    console.log(this.userId + ' is activated');
    this.authService.activateAccount(this.userId).subscribe(
      data => {
        console.log(data);
      },
      error => {
        console.log(error);
      }
    );
  }


}
