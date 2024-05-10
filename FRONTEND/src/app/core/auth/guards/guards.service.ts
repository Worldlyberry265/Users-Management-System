import { CanActivate, Router } from "@angular/router";
import { Observable, catchError } from 'rxjs';
import { AuthService } from "../auth.service";
import { Injectable } from '@angular/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable()
export class AuthGuard implements CanActivate {

    constructor(private authService: AuthService,
        private router: Router) {

    }
    // Observable<boolean> | Promise<boolean> | boolean
    //subscription for checktokenisexpired
    canActivate(): Observable<boolean> | Promise<boolean> | boolean { // route: ActivatedRouteSnapshot, state: RouterStateSnapshot
        return this.authService.isTokenExpired().pipe(
          map((isExpired: boolean) => {
            if (isExpired) {
              this.router.navigate(['/login']);
              return false;
            } else {
              return true;
            }
          }),
          catchError((error: any) => {
            console.error('Error occurred while checking token expiration:', error);
            return of(false);
          })
        );
      }
}