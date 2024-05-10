import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Data, Router } from '@angular/router';
import { AuthService } from 'src/app/core/auth/auth.service';
import { User } from 'src/app/model/user';


interface LoginError {
  // status: number;
  error: string;
  // message: string;
  // path: string;
  // timestamp: Date,
}

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.css']
})
export class EditUserComponent implements OnInit {
  Passwordregex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{6,}$/;

  Id : number;
  Username: string;
  Email: string;
  password: string = null;
  Address: string;
  Roles: string;

  WrittenError = '';
  ErrorFound = false;

  hidePassword: boolean = true;

  constructor(private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService) { }

  ngOnInit(): void {
    this.route.data.subscribe(
      (data: Data) => {
        this.Id = data['user'].id;
        this.Username = data['user'].username;
        this.Email = data['user'].email;
        this.Address = data['user'].address;
        this.Roles = data['user'].roles;
        // console.log(this.Username);
        // console.log(this.Email);
        // console.log(this.Address);
        // this.user = data['user'];
        // console.log(this.user.getEmail());
      },
      (error) => {
        console.log(error);
      }
    );


  }
//   if(this.Passwordregex.test(this.password)) {
//   this.ErrorFound = false;
//   this.authService.SignUp(this.Username, this.Email, this.password).subscribe(
//     (response: string) => {
//       alert(response);
//       this.router.navigate(['/login']);
//     },
//     (error: LoginError) => {
//       this.ErrorFound = true;
//       const errorMessage = JSON.parse(error.error).message;
//       this.WrittenError = errorMessage;
//       console.error("error: " + error.error);
//     }
//   );
// } else {
//   this.WrittenError = 'Your password has to be at least 6 characters long and contain at least 1 lowercase letter, 1 uppercase letter, 1 number, and 1 of these !@#$%^&*';
//   this.ErrorFound = true;
// }


onEdit() {
  if (this.Passwordregex.test(this.password) || this.password === null) {
    this.ErrorFound = false;
    const returnedUser = new User(
      this.Id, 
      this.Username, 
      this.Email, 
      this.password, 
      this.Address, 
      this.Roles
    );
    this.authService.UpdateUser(this.Id, this.Username, this.Email, this.password, this.Address, this.Roles).subscribe(
      (response: string) => {
        alert(response);
        this.router.navigate(['/dashboard/usersList']);
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
  onNavigate() {
    this.router.navigate(['/homepage']);
  }
}
