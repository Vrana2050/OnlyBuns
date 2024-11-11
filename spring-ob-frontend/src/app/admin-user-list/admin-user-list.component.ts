import { Component, OnInit } from '@angular/core';
import { UserService } from '../service';
import { User } from '../model/user.model';

@Component({
  selector: 'app-admin-user-list',
  templateUrl: './admin-user-list.component.html',
  styleUrls: ['./admin-user-list.component.css']
})
export class AdminUserListComponent implements OnInit {
  users: User[] = [];
  justarray: User[] = [];
  pageSize: number = 5;
  totalUsers: number = 0;
  totalPages: number = 0;

  filter = {
    firstName: '',
    lastName: '',
    email: '',
    minPosts: 0,
    maxPosts: 100000,
    sortBy: 'email',
    sortDirection: 'asc',
    page: 0,
    size: this.pageSize
  };

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.searchAndSortUsers();
  }

  loadUsers() {
    this.userService.getAll()
      .subscribe(
        data => {
          console.log(data); 
          this.justarray = data; 
          
          this.totalUsers = data.totalCount || this.justarray.length; 
          this.totalPages = Math.ceil(this.totalUsers / this.pageSize);
          console.log(this.totalPages + this.totalUsers)
        },
        error => {
          console.log(error);
        }
      );
    this.searchAndSortUsers();
}


searchAndSortUsers() {
  this.userService.getFilteredUsers(this.filter).subscribe((response) => {
    console.log('Response:', response);
    console.log('Is Array:', Array.isArray(response)); 
    this.users = response.content;       // The paginated list of users
    this.totalUsers = response.totalElements; // Total number of users
    this.totalPages = response.totalPages;
    console.log("TOTAL PAGES" + this.totalPages);
  });
  
  
  
}
onSearch() {
  this.searchAndSortUsers();
}


onSort(sortBy: string) {
  console.log("sad" + sortBy + this.filter.sortDirection)

  if (this.filter.sortBy === sortBy) {
    this.filter.sortDirection = this.filter.sortDirection === 'asc' ? 'desc' : 'asc';
  } else {
    this.filter.sortBy = sortBy;
    this.filter.sortDirection = 'asc';
  }
  console.log("promenjeno" + sortBy + this.filter.sortDirection)
  this.searchAndSortUsers();
}

  
  get totalPagesArray(): number[] {
    return this.totalPages ? Array.from({ length: this.totalPages }, (_, i) => i ) : [];
  }
  

  goToPage(page: number) {
    if (page !== this.filter.page) {
      this.filter.page = page;
      this.searchAndSortUsers();
    }
  }

  nextPage() {
    if (this.filter.page < this.totalPages -1) {
      this.filter.page++;
      console.log("ONA KOJU SALJEM IZ FILTERA" + this.filter.page)
      this.searchAndSortUsers();
    }
  }

  previousPage() {
    console.log(this.filter.page + "STRANICE JE")
    if (this.filter.page  > 0) {
      console.log(this.filter.page + "PRI PREVIOUSU")
      this.filter.page--;
      this.searchAndSortUsers();
    }
  }
}
