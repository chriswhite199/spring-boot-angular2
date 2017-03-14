import { Component, OnInit } from '@angular/core';
import { Headers, Http, RequestOptions } from '@angular/http';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../auth.service';

@Component( {
    selector: 'app-login',
    templateUrl: './app-login.component.html'
})
export class AppLoginComponent {

    model: any = {};
    loading = false;
    returnUrl: string;

    constructor(
        //private route: ActivatedRoute,
        private router: Router,
        private authService: AuthService,
        //private alertService: AlertService 
    ) { }

    ngOnInit() {
        // reset login status
        //this.authenticationService.logout();

        // get return url from route parameters or default to '/'
        //this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
    }

    login() {
        this.authService.login( this.model.username, this.model.password ).subscribe(
            data => {
                this.router.navigate( [this.returnUrl] );
            },
            error => {
                //this.alertService.error( error );
                this.loading = false;
            });
        var body = { 'username': this.model.username, 'password': this.model.password };
        //this.http.post( '/login', body ).toPromise().then( console.log )
    }
}
