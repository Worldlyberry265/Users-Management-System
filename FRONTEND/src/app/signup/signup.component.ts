import { Component } from '@angular/core';
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
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent {

  // UserRegex = /^[A-Za-z ]+$/;
  // EmailRegex = /^[a-zA-Z][a-zA-Z0-9._-]*@[a-zA-Z0-9-]+\.[a-zA-Z]{2,4}$/;
  Passwordregex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{6,}$/;
  Username: string;
  Email: string;
  password: string;

  WrittenError = '';

  ErrorFound = false;

  hidePassword: boolean = true;

  constructor(private authService: AuthService,
    private router: Router) { }

  onSignUp(): void {
    if (this.Passwordregex.test(this.password)) {
      this.ErrorFound = false;
      this.authService.SignUp(this.Username, this.Email, this.password).subscribe(
        (response: string) => {
          alert(response);
          this.router.navigate(['/login']);
        },
        (error: LoginError) => {
          this.ErrorFound = true;
          const errorMessage = JSON.parse(error.error).message;
          this.WrittenError = errorMessage;
          console.error("error: " + error.error);
        }
      );
    } else {
      this.WrittenError = 'Your password has to be at least 6 characters long and contain at least 1 lowercase letter, 1 uppercase letter, 1 number, and 1 of these !@#$%^&*';
      this.ErrorFound = true;
    }
  }

  togglePasswordVisibility(): void {
    this.hidePassword = !this.hidePassword;
  }
}
//USE *ngIf to check regex;