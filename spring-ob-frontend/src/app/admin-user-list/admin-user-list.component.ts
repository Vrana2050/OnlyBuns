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
  currentPage: number = 1;
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
  };

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers() {
    this.userService.getAll()
      .subscribe(
        data => {
          console.log(data); 
          this.users = data; 
          
          this.totalUsers = data.totalCount || this.users.length; 
          this.totalPages = Math.ceil(this.totalUsers / this.pageSize);
        },
        error => {
          console.log(error);
        }
      );
}


searchAndSortUsers() {
  this.userService.getFilteredUsers(this.filter).subscribe((response) => {
    console.log('Response:', response);
    console.log('Is Array:', Array.isArray(response)); 
    this.users = response; 
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
    return this.totalPages ? Array.from({ length: this.totalPages }, (_, i) => i + 1) : [];
  }
  

  goToPage(page: number) {
    if (page !== this.currentPage) {
      this.currentPage = page;
      this.loadUsers();
    }
  }
  /*
  onSearch() {
    this.currentPage = 1;
    this.loadUsers();
  }

  onSort(sortBy: string) {
    this.filter.sortBy = sortBy;
    this.filter.sortOrder = this.filter.sortOrder === 'asc' ? 'desc' : 'asc';
    this.loadUsers();
  }*/

  nextPage() {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.loadUsers();
    }
  }

  previousPage() {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.loadUsers();
    }
  }
}
