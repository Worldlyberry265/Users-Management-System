import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../core/auth/auth.service';
import { Router } from '@angular/router';

interface LoginError {
  // status: number;
  error: string;
  // message: string;
  // path: string;
  // timestamp: Date,
}

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  Email: string;
  password: string;

  WrittenError = '';
  ErrorFound = false;

  constructor(private http: HttpClient,
    private authservice: AuthService,
    private router: Router) { }

  hidePassword: boolean = true;

  ngOnInit() { }

  togglePasswordVisibility(): void {
    this.hidePassword = !this.hidePassword;
  }
  login(): void {
    this.authservice.login(this.Email, this.password).subscribe(
      (response: string) => {
        console.log('jwt: ' + response);
        localStorage.setItem('jwt', response);
        // this.authservice.setLoginStatus(true); 
        this.router.navigate(['/dashboard']);
      },
      
      (error: LoginError) => {
        this.ErrorFound = true;
        const errorMessage = JSON.parse(error.error).message;
        this.WrittenError = errorMessage;
        console.error("error: " + error.error);
      }

    );
  }
  SignNav() {
    this.router.navigate(['/signup']);
  }
}
