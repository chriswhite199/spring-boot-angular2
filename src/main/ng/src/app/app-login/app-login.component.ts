import { Component, OnInit } from '@angular/core';
import { Headers, Http, RequestOptions } from '@angular/http';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../auth/auth.service';

@Component( {
    selector: 'app-login',
    templateUrl: './app-login.component.html'
})
export class AppLoginComponent {
    static URL = 'login';
    
    model: any = {};
    loading = false;
    error = '';
    returnUrl: string;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private authService: AuthService,
        //private alertService: AlertService 
    ) { }

    ngOnInit() {
        // reset login status
        this.authService.logout();

        // get return url from route parameters or default to '/'
        this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
    }

    login() {
        this.loading = true;
        this.authService.login( this.model.username, this.model.password ).subscribe(
            data => {
                console.log( 'Redirecting to ' + this.returnUrl );
                this.router.navigate( [this.returnUrl] );
            },
            error => {
                this.error = 'Invalid username / password';
                this.loading = false;
            });
        var body = { 'username': this.model.username, 'password': this.model.password };
    }
}
