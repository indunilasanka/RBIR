import { Injectable } from '@angular/core';
import { Http, RequestOptions, Response, Headers } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';


// import { Authentication } from '../../../models/request.model';
import { environment } from '../../../environments/environment';


@Injectable()
export class CreateUserService {

  
    private host: string = environment.host;
    private port: string = environment.port;
    private endcall: string = '/user'; // end call --------------------
    
    private baseUrl: string = this.host +':'+ this.port + this.endcall;

  constructor(private http: Http) {
  }

  private getHeaders() {
    // I included these headers because otherwise FireFox
    // will request text/html instead of application/json
    let headers = new Headers();
    headers.append('Accept', 'application/json');
    headers.append('Access-Control-Allow-Origin', '*');
    return headers;
  }

  createUser(name: string, lname: string, email: string, password: string, level: string): Observable<Object[]> {

    let formData: FormData = new FormData();
    formData.append('name', name);    
    formData.append('lname', lname);
    formData.append('email', email);
    formData.append('password', password);
    formData.append('level', level);

    return this.http.post(this.baseUrl+"/create-user", formData, { headers: this.getHeaders()})
      .map(response => response.json())
      .catch(this.handleError);
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
