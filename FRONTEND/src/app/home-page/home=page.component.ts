import { Component } from '@angular/core';
import { AuthService } from '../core/auth/auth.service';
// import { User } from '../model/user';
// import { MatTableDataSource } from '@angular/material/table';
// import { MatPaginator } from '@angular/material/paginator';
// import { MatSort } from '@angular/material/sort';
import { Router, Event, NavigationEnd } from '@angular/router'; // Import the necessary classes

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent {
  triggered = true;

  constructor(private authService: AuthService, private router: Router) {
    this.router.events.subscribe((event: Event) => {
      if (event instanceof NavigationEnd) {
        this.triggered = !this.router.url.includes('/usersList');
      }
    });
  }

  logout() {
    this.authService.logout();
  }
  onNavigate() {
    this.router.navigate(['/editUser']);
  }

}





    // Users: User[];
  // dataSource: MatTableDataSource<User>;
  // displayedColumns = ['username', 'email', 'address', 'roles', 'Edit'];
  // UsersCount: number;
  // pageSize: number = 10;

  // @Output() user = new EventEmitter<User>();

  // ErrorFound = false;
  // WrittenError: string;
  // filter: string;

  // @ViewChild(MatPaginator) paginator: MatPaginator;
  // @ViewChild(MatSort) sort: MatSort;

  // ngOnInit(): void {
  //   this.getData();
  // }

  // ngAfterViewInit() {
  //   this.sort.sortChange.subscribe(() => {
  //     let sortParameter: string = this.sort.active + this.sort.direction;
  //     this.getUsers(this.paginator.pageIndex, this.paginator.pageSize, sortParameter);
  //   });

  //   this.paginator.page.subscribe(() => {
  //     let sortParameter: string = this.sort.active + this.sort.direction;
  //     if (sortParameter === "undefined") {
  //       sortParameter = "Default";
  //     }
  //     this.getUsers(this.paginator.pageIndex, this.paginator.pageSize, sortParameter);
  //   });
  // }

  // getData(): void {
  //   console.log("GET DATA HERE");
  //   const pageSize = 10;
  //   this.userService.getUsersCount().subscribe(
  //     (UserCount: number) => {
  //       this.UsersCount = UserCount;
  //       this.getUsers(0, pageSize, 'Default');
  //     },
  //     (error) => {
  //       console.log(error);
  //     }
  //   );
  // }

  // getUsers(Index?: number, PageSize?: number, sortParameter?: string): void {
  //   this.userService.getUsers(Index, PageSize, sortParameter).subscribe(
  //     (data: User[]) => {
  //       this.Users = data;
  //       this.dataSource = new MatTableDataSource(this.Users);
  //     },
  //     (error) => {
  //       console.log(error);
  //     }
  //   );
  // }
  // applyFilter(filterVaule: string): void {
  //   this.ErrorFound = false;
  //   if (filterVaule.startsWith('.')) {
  //     this.ErrorFound = true;
  //     this.WrittenError = "You can't start with a point";
  //   }
  //   else {
  //     if (this.authService.containsSpecialCharacters(filterVaule)) {
  //       this.ErrorFound = true;
  //       this.WrittenError = "You can't use any quotation, slash, or any of these characters: %_*[]-=;";
  //       return;
  //     }
  //     if (filterVaule.trim() === '') {
  //       this.getUsers(0, 10, "Default");
  //     } else {
  //       this.userService.FilterBy(filterVaule).subscribe(
  //         (data: User[]) => {
  //           this.Users = data;
  //           this.dataSource = new MatTableDataSource(this.Users);
  //         },
  //         (error) => {
  //           console.log("ERROR: " + error.message);
  //           this.ErrorFound = true;
  //           this.WrittenError = error.message;
  //         }
  //       );
  //     }
  //   }
  // }


  // editUser(element: User) {
  //   this.user.emit(element);
  //   this.router.navigate(['/editUser']);
  //   // or pass it through navigate but fix it at routing
  // }