import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';
import { User } from '../model/user';
import { Observable } from 'rxjs';
import { UserService } from '../model/UserService';

@Injectable({
  providedIn: 'root'
})
export class UserResolverService implements Resolve<User> {

  constructor(private userService : UserService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): User | Observable<User> | Promise<User> {
      return this.userService.getUser(+route.params['id']);
  }
}
