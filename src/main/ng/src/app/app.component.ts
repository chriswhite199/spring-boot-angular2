import { Component } from '@angular/core';
import { Headers, Http, RequestOptions, URLSearchParams } from '@angular/http';
import 'rxjs/add/operator/toPromise';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Login page!';

  constructor(private http: Http) { }

  doLogin() {
    console.log('posting')
    var headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');
    let params: URLSearchParams = new URLSearchParams();
    params.set('username', 'user')
    params.set('password', 'password')

    let options = new RequestOptions({ headers: headers });
    this.http.post('/login', params.toString(), options).toPromise().then(console.log)
  }
}
