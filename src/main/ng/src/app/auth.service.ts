import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';
import 'rxjs/add/operator/map'

@Injectable()
export class AuthService {
    username: string;
    password: string;
    jwt: string;
    jwtExpires: number;

    constructor( private http: Http ) { }

    login( username: string, password: string ) {
        this.username = username;
        this.password = password;

        return this.http.post( '/login', JSON.stringify( { username: username, password: password }) )
            .map(( response: Response ) => {
                var respBody = response.json()
                if ( respBody && respBody.jwt ) {
                    this.jwt = respBody.jwt;
                    this.jwtExpires = respBody.jetExpires;
                }
            });
    }

    isAuthenticated(): boolean {
        return this.jwt != null;
    }

    logout() {
        this.jwt = null;
    }
}
