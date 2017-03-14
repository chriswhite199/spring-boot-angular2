import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { DecimalPipe } from '@angular/common';
import { Observable } from 'rxjs/Rx';

import { AuthService } from '../auth/auth.service';

@Component( {
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {
    ttl: number;

    constructor( private authService: AuthService, private decimalPipe: DecimalPipe ) { }

    ngOnInit() {
        this.updateTtl();
        Observable.interval( 1000 ).subscribe(( x ) => {
            this.updateTtl();
        });
    }

    updateTtl() {
        this.ttl = this.authService.getTokenExpires() - Date.now();
    }

    renewToken() {
        this.authService.renewToken().subscribe(
            data => {
                console.log( 'Renewed token' );
            },
            error => {
                console.error( 'Failed to renew token' );
            });
    }
    
    getTtlText():string {
        var ttlMins = this.ttl/(60*1000)|0;
        var ttlSecs = ((this.ttl/1000)%60)|0;
        return ttlMins + ':' + this.decimalPipe.transform(ttlSecs, '2.0-0');
    }
    
    isTokenExpired(): boolean {
        return !this.authService.isAuthenticated();
    }
}
