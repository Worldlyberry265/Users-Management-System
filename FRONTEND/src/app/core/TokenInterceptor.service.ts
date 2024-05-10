import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable, mergeMap, tap } from 'rxjs';
import { Router } from '@angular/router';
import { AuthService } from './auth/auth.service';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService, private router: Router) { }


  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    if (request.url.includes('/dashboard')) {
      return this.authService.isTokenExpired().pipe(
        tap((isExpired: boolean) => {
          if (isExpired) {
            alert("Your session has expired");
            this.router.navigate(['/login']);
          } else {
            console.log('Token is not expired');
          }
        }),
        mergeMap(() => {
          const newRequest = request.clone();
          return next.handle(newRequest);
        })
      );
    } else {
      return next.handle(request);
    }
  }
}
