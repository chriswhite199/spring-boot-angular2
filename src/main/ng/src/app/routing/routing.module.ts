import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AppLoginComponent } from '../app-login/app-login.component';
import { DashboardComponent } from '../dashboard/dashboard.component';
import { AuthGuard } from '../auth/auth.guard';

const routes: Routes = [
    { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
    { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },
    { path: AppLoginComponent.URL, component: AppLoginComponent },
    
    // catch all
    { path: '**', redirectTo: '' }
];
@NgModule( {
    imports: [RouterModule.forRoot( routes )],
    exports: [RouterModule]
})
export class AppRoutingModule { }
