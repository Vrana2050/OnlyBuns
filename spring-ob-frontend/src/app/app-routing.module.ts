import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { SignUpComponent } from './sign-up/sign-up.component';
import { AdminUserListComponent } from './admin-user-list/admin-user-list.component';
import { PostComponent } from './post/post.component';
import { ActivateAccountComponent } from './activate-account/activate-account.component';
import { SuccessfullyActivatedComponent } from './successfully-activated/successfully-activated.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { OtherUserProfileComponent } from './other-user-profile/other-user-profile.component';
import { TrendingComponent } from './trending/trending.component';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'signup',
    component: SignUpComponent,
  },
  {
    path: 'admin-user-list',
    component: AdminUserListComponent,
  },
  {
    path: 'post',
    component: PostComponent,
  },
  {
    path: 'activate-account',
    component: ActivateAccountComponent,
  },
  {
    path: 'successfully-activated',
    component: SuccessfullyActivatedComponent,

  },
  {
    path: 'profile',
    component: UserProfileComponent,
  },
  {
    path: 'other-user-profile',
    component: OtherUserProfileComponent,
  },
  {
    path: "trending",
    component: TrendingComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
