import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { baseUrl } from 'src/enviroments/enviroments';
import { AuthClient } from '../core/auth/auth.client';
import { User } from './user';

@Injectable({
    providedIn: 'root'
})
export class UserService {

    constructor(private authClient: AuthClient) { }

    getUsers(Index?: number, PageSize?: number, field?: string): Observable<User[]> {
        return this.authClient.getUsers(Index, PageSize, field);
    }
    getUsersCount(): Observable<number> {
        return this.authClient.getUsersCount();
    }
    FilterBy(filterValue : string): Observable<User[]> {
        return this.authClient.filterByValue(filterValue);
    }
    getUser(id : number) {
        return this.authClient.getUser(id);
    }
}
