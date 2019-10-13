import { Injectable } from '@angular/core';
import { Http, Headers, RequestOptions, Response } from '@angular/http';
import { Observable } from 'rxjs';
import 'rxjs/add/operator/map';
import { environment } from '../../environments/environment';
 
/**
 * Created by Kelum Bandara on 6/1/2017.
*/


import { AuthenticationService } from '../services/authentication.service';


@Injectable()
export class UserService {


    private host: string = environment.host;
    private port: string = environment.port;
    private endcall: string = '/user';
    
    private baseUrl: string = this.host +':'+ this.port + this.endcall;

    constructor(
        private http: Http,
        private authenticationService: AuthenticationService) {
    }
 
    getUser(): Observable<any[]> {
        // add authorization header with jwt token
        let headers = new Headers({ 'Authorization': 'Bearer ' + localStorage.getItem('token') });
        let options = new RequestOptions({ headers: headers });
 
        
        // get users from api
        return this.http.get(this.baseUrl+'/getProfile?username='+localStorage.getItem('userName'), options)
            .map((response: Response) => response.json());
    }
}
