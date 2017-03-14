import { Injectable } from '@angular/core';
import { Http, Headers, RequestOptions, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map'

import { CurrentUser } from './current-user';

@Injectable()
export class AuthService {
    readonly SESSION_USER_KEY = 'currentUser';

    private currentUser: CurrentUser;

    constructor( private http: Http ) {
        this.currentUser = JSON.parse( sessionStorage.getItem( this.SESSION_USER_KEY ) );
    }

    login( username: string, password: string ) {
        let headers = new Headers( { 'Content-Type': 'application/json; charset=UTF-8' });
        let options = new RequestOptions( { headers: headers });

        return this.http.post( '/login', JSON.stringify( { username: username, password: password }), options )
            .map(( response: Response ) => {
                var respBody = response.json()
                if ( respBody && respBody.jwt ) {
                    this.currentUser = new CurrentUser( username, password, respBody.jwt, Date.parse( respBody.jwtExpires ) );
                    sessionStorage.setItem( this.SESSION_USER_KEY, JSON.stringify( this.currentUser ) );
                    return true;
                } else {
                    this.logout();
                }

                return false;
            });
    }
    
    getUsername(): string {
        return this.currentUser != null && this.currentUser.username;
    }

    renewToken(): Observable<Boolean> {
        if ( this.currentUser != null ) {
            return this.login( this.currentUser.username, this.currentUser.password )
        } else {
            return new Observable( observer => observer.next( false ) );
        }
    }

    isAuthenticated(): boolean {
        return this.currentUser != null && this.currentUser.jwtExpires > Date.now();
    }
    
    getTokenExpires(): number {
        return this.currentUser ? this.currentUser.jwtExpires : 0;
    }

    logout() {
        sessionStorage.setItem( this.SESSION_USER_KEY, null );
        this.currentUser = null;
    }

    getWhoami() {
        let headers = new Headers( { 'Authorization': 'Bearer ' + this.currentUser.jwt });
        let options = new RequestOptions( { headers: headers });

        return this.http.get( 'api/whoami', options ).map(( response: Response ) => response.json() );
    }
}
