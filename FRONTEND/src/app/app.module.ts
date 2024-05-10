import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatMenuModule } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';

import { JwtModule } from "@auth0/angular-jwt";
import { AppComponent } from './app.component';
import { LoginComponent } from './logging/login.component';
import { HomePageComponent } from './home-page/home=page.component';
import { AuthService } from './core/auth/auth.service';
import { JwtHelperService } from '@auth0/angular-jwt';
import { AppRoutingModule } from './core/app-routing.module';
import { SignupComponent } from './signup/signup.component';
import { AuthClient } from './core/auth/auth.client';
import { AuthGuard } from './core/auth/guards/guards.service';
import { CdkTableModule } from '@angular/cdk/table';
import { TokenInterceptor } from './core/TokenInterceptor.service';
import { EditUserComponent } from './home-page/users-list/edit-user/edit-user.component';
import { UsersListComponent } from './home-page/users-list/users-list.component';

export function tokenGetter() {
  return localStorage.getItem("jwt");
}

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomePageComponent,
    SignupComponent,
    EditUserComponent,
    UsersListComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    HttpClientModule ,
    AppRoutingModule,
    MatToolbarModule,
    MatMenuModule,
    MatIconModule,
    MatPaginatorModule,
    MatTableModule,
    CdkTableModule,
    MatSortModule,    
    JwtModule.forRoot({
      config: {
        tokenGetter: tokenGetter,
        allowedDomains: ['*'],
        disallowedRoutes: [],
      },
    }),
  ],
  providers: [AuthService,JwtHelperService,AuthClient,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true,
    },
    AuthGuard], //Didnt add UserService bcz providedIn: 'root'
  bootstrap: [AppComponent]
})
export class AppModule { }
