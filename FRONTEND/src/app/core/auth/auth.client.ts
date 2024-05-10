import { Injectable } from '@angular/core';
import { Observable, timer } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { baseUrl } from 'src/enviroments/enviroments';
import { User } from 'src/app/model/user';

@Injectable()
export class AuthClient {

    AuthURL = baseUrl + 'authenticate';
    SaveURL = baseUrl + 'SaveUser';
    UsersURL = baseUrl + 'dashboard/getUsers/';
    PageURL = baseUrl + 'dashboard/getUserCount';
    FilterURL = baseUrl + 'dashboard/filterBy/';
    getUserURL = baseUrl + 'dashboard/usersList/getUser/';
    editUserURL = baseUrl + 'dashboard/usersList/editUser';

    constructor(private http: HttpClient) {
    }
    login(loginData): Observable<string> {
        return this.http.post(this.AuthURL, loginData, { responseType: "text" });
    }

    SignUp(SignUpData): Observable<string> {
        return this.http.post(this.SaveURL, SignUpData, { responseType: "text" });
    }
    getUsers(Index?: number, PageSize?: number, field?: string): Observable<User[]> {
        return this.http.get<User[]>(this.UsersURL + field + "/" + Index + "/" + PageSize);
    }
    getUsersCount() : Observable<number>{
        return this.http.get<number>(this.PageURL);
    }
    filterByValue(filterValue : string): Observable<User[]> {
        return this.http.get<User[]>(this.FilterURL + filterValue);
    }
    getUser(id : number) : Observable<User>{
        return this.http.get<User>(this.getUserURL + id);
    }
    updateUser(UpdateData): Observable<string> {
        return this.http.post(this.editUserURL, UpdateData, { responseType: "text" });
    }

}

