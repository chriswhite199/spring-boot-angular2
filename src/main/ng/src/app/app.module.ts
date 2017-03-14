import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { AppRoutingModule } from './routing/routing.module';
import { AuthGuard } from './auth/auth.guard';
import { AuthService } from './auth/auth.service';

import { AppComponent } from './app.component';
import { AppLoginComponent } from './app-login/app-login.component';
import { DashboardComponent } from './dashboard/dashboard.component';

@NgModule( {
    declarations: [
        AppComponent,
        AppLoginComponent,
        DashboardComponent
    ],
    imports: [
        BrowserModule,
        FormsModule,
        HttpModule,
        AppRoutingModule
    ],
    providers: [
        AuthGuard,
        AuthService
    ],
    bootstrap: [AppComponent]
})
export class AppModule { }
