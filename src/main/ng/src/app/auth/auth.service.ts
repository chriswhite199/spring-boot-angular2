import { Injectable } from '@angular/core';
import { Http, Headers, RequestOptions, Response } from '@angular/http';
import 'rxjs/add/operator/map'

import { CurrentUser } from './current-user';

@Injectable()
export class AuthService {
    
    currentUser: CurrentUser;

    constructor( private http: Http ) { 
        this.currentUser = JSON.parse(sessionStorage.getItem('currentUser'));
    }

    login( username: string, password: string ) {
        let headers = new Headers( { 'Content-Type': 'application/json; charset=UTF-8' });
        let options = new RequestOptions( { headers: headers });

        return this.http.post( '/login', JSON.stringify( { username: username, password: password }), options )
            .map(( response: Response ) => {
                var respBody = response.json()
                if ( respBody && respBody.jwt ) {
                    this.currentUser = new CurrentUser(username, password, respBody.jwt, Date.parse(respBody.jwtExpires));
                    sessionStorage.setItem('currentUser', JSON.stringify(this.currentUser));
                    return true;
                } else {
                    this.logout();
                }
                
                return false;
            });
    }

    isAuthenticated(): boolean {
        return this.currentUser != null && this.currentUser.jwtExpires > Date.now();
    }

    logout() {
        this.currentUser = null;
    }
}
