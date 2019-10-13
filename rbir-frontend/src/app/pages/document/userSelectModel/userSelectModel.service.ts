import {Injectable} from '@angular/core';
import { Http,RequestOptions, Response,Headers} from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';


import { Authentication } from '../../../models/request.model';
import { environment } from '../../../../environments/environment';


export class Responce {
  public email: string;
  public request: string;
  public requestID: string;
  
}


@Injectable()
export class UserSelectModelService {

    private host: string = environment.host;
    private port: string = environment.port;

    //base url to contact user endpoint
    private endcall1: string = '/user';
    private baseUrl1: string = this.host +':'+ this.port + this.endcall1;

    //base url to contact user results endpoint
    private endcall2: string = '/result'; 
    private baseUrl2: string = this.host +':'+ this.port + this.endcall2;

  constructor(private http : Http) {
  }

  private getHeaders(){
    // I included these headers because otherwise FireFox
    // will request text/html instead of application/json
    let headers = new Headers();
    headers.append('Accept', 'application/json');
    return headers;
  }



  getHighEndUsers(securityLevel:string): Observable<Object[]> {
    return this.http.get(this.baseUrl1+"/getRankedOfficers?documentlevel="+securityLevel,{headers: this.getHeaders()})
      .map(response => response.json())
      .catch(this.handleError);
  }

  passResultToHighEndUser(adminUserEmail:string, reqId:string,searchId:string,securityLevel:string): Observable<Object[]> {
   
    console.log(adminUserEmail,reqId,searchId,securityLevel);
    // let urlSearchParams = new URLSearchParams();
    // urlSearchParams.append('email', email);    
    // urlSearchParams.append('request', request);
    // urlSearchParams.append('requestid', requestId);
    let formData:FormData = new FormData();
    formData.append('adminUserEmail', adminUserEmail);
    formData.append('reqId', reqId);
    formData.append('searchId', searchId);
    formData.append('securityLevel', securityLevel);

    return this.http.post(this.baseUrl2+"/add",formData,{headers: this.getHeaders()})
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
