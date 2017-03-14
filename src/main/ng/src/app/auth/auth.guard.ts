import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import { AuthService } from './auth.service';
import { AppLoginComponent } from '../app-login/app-login.component';

@Injectable()
export class AuthGuard implements CanActivate {
    
  constructor(private router: Router, private authService:AuthService) { }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): boolean {
        if (this.authService.isAuthenticated()) {
            // logged in so return true
            return true;
        }

        // not logged in so redirect to login page with the return url
        this.router.navigate([ '/' + AppLoginComponent.URL], { queryParams: { returnUrl: state.url }});
        return false;
  }
}
