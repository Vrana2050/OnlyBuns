import { BrowserModule } from '@angular/platform-browser';
import { NgModule, NO_ERRORS_SCHEMA } from '@angular/core';
import {HttpClientModule} from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { CardComponent } from './card/card.component';
import { HomeComponent } from './home/home.component';
import { HeaderComponent } from './header/header.component';
import { LoginComponent } from './login/login.component';
import { SignUpComponent } from './sign-up/sign-up.component';

import {FormsModule, ReactiveFormsModule} from '@angular/forms';

import {ApiService} from './service/api.service';
import {FooService} from './service/foo.service';
import {AuthService} from './service/auth.service';
import {UserService} from './service/user.service';
import {ConfigService} from './service/config.service';

import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { TokenInterceptor } from './interceptor/TokenInterceptor';
import { AngularMaterialModule } from './angular-material/angular-material.module';
import { AdminUserListComponent } from './admin-user-list/admin-user-list.component';
import { PostComponent } from './post/post.component';
import { MapComponent } from './shared/map/map.component';
import { ActivateAccountComponent } from './activate-account/activate-account.component';
import { SuccessfullyActivatedComponent } from './successfully-activated/successfully-activated.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { OtherUserProfileComponent } from './other-user-profile/other-user-profile.component';
import { TrendingComponent } from './trending/trending.component';

@NgModule({
  declarations: [
    AppComponent,
    CardComponent,
    HomeComponent,
    HeaderComponent,
    LoginComponent,
    SignUpComponent,
    AdminUserListComponent,
    PostComponent,
    MapComponent,
    ActivateAccountComponent,
    SuccessfullyActivatedComponent,
    UserProfileComponent,
    OtherUserProfileComponent,
    TrendingComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    NoopAnimationsModule,
    AngularMaterialModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  providers: [ 
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    },
    FooService,
    AuthService,
    ApiService,
    UserService,
    ConfigService,
  ],
  bootstrap: [AppComponent],
  schemas: [NO_ERRORS_SCHEMA]
})
export class AppModule { }
