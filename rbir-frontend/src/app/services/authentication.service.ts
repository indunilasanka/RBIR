/**
 * Created by Kelum Bandara on 6/1/2017.
*/


import { Injectable } from '@angular/core';
import { Http, Headers, Response, RequestOptions } from '@angular/http';
import { Observable } from 'rxjs';
import 'rxjs/add/operator/map';
import { environment } from '../../environments/environment';
 
@Injectable()
export class AuthenticationService {
    
    public token: string;

    private host: string = environment.host;
    private port: string = environment.port;
    private endcall: string = '/oauth/token';
    
    private baseUrl: string = this.host +':'+ this.port + this.endcall;
 
    constructor(private http: Http) {
        // set token if saved in local storage
        var currentUser = JSON.parse(localStorage.getItem('currentUser'));
        this.token = currentUser && currentUser.token;
    }
     
    private getHeaders(){
        // I included these headers because otherwise FireFox
        // will request text/html instead of application/json
        let headers = new Headers();
        headers.append('Accept', 'application/json');

        return headers;
    }

    login(username: string, password: string): Observable<boolean> {

        let headers = new Headers({
            "Content-Type": "application/x-www-form-urlencoded",
            "Accept": "application/json",
            "Authorization": "Basic " + btoa("testjwtclientid" + ':' + "MaYzkSjmkzPC57L")
        });

        let options = new RequestOptions({ headers: headers });

        let client = "username=" + username + "&password=" + password + "&grant_type=password&" +
            "client_secret=MaYzkSjmkzPC57L&client_id=testjwtclientid";

        return this.http.post(this.baseUrl, client, options)
            .map((response: Response) => {

                console.log(response);
                
                // login successful if there's a jwt token in the response
                let token = response.json() && response.json().access_token;
                console.log(token);
                if (token) {
                    // set token property
                    this.token = token;
 
                    // store username and jwt token in local storage to keep user logged in between page refreshes
                    localStorage.setItem('currentUser', JSON.stringify({ username: username, token: response.json().access_token }));
                    console.log(token)

                    localStorage.setItem('userName',username);
                    localStorage.setItem('token',response.json().access_token);
 
                    // return true to indicate successful login
                    console.log("posting finished");
                    return true;
                } else {
                    // return false to indicate failed login
                    console.log("posting finished");
                    return false;
                }
            });            
    }
 
    logout(): void {
        // clear token remove user from local storage to log user out
        this.token = null;
        localStorage.removeItem('currentUser');
    }
    
    private handleError (error: Response | any) {
    // In a real world app, you might use a remote logging infrastructure
    let errMsg: string;
    if (error instanceof Response) {
      const body = error.json() || '';
      const err = body.error || JSON.stringify(body);
      errMsg = `${error.status} - ${error.statusText || ''} ${err}`;
    } else {
      errMsg = error.message ? error.message : error.toString();
    }
    console.error(errMsg);
    return Observable.throw(errMsg);
  }
}
