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
export class RequestService {

  
    private host: string = environment.host;
    private port: string = environment.port;
    private endcall: string = '/request';
    
    private baseUrl: string = this.host +':'+ this.port + this.endcall;

  constructor(private http : Http) {
  }

  private getHeaders(){
    // I included these headers because otherwise FireFox
    // will request text/html instead of application/json
    let headers = new Headers();
    headers.append('Accept', 'application/json');
    headers.append('Access-Control-Allow-Origin', '*');
    return headers;
  }

  private _requests = [
    {
      user: {
        name : 'Saranga',
        occupation: 'Student',
        address: 'No:2, Rathmahara, Gonapinuwala',
        telephone: '0714578283',
        email: 'saeanga@gmail.com',
        profPic: '',
      },
      authenticator: {
        name : 'perera',
        occupation: 'Accountant',
        address: 'Bandaranayaka Mawata, Katubedda, Moratuwa',
        telephone: '0114008283',
        email: 'perera@gmail.com',
        profPic: '',
      },
      header: 'Tax details',
      content: '2017 tax report asjdlks kajsdj asjdlkjklasd adsadsad zxccasdsd',
      attachment: null,
      authentication: Authentication.NOTNEEDED,
      finished: false,
    },
  ];



  getRequest(): Observable<Object[]> {
    return this.http.get(this.baseUrl+"/get-requests",{headers: this.getHeaders()})
      .map(response => response.json())
      .catch(this.handleError);
  }

  deleteRequest(email:string, requestId:string,request:string): Observable<Object[]> {

    console.log(email,requestId,request);

    let urlSearchParams = new URLSearchParams();
    urlSearchParams.append('email', email);    
    urlSearchParams.append('request', request);
    urlSearchParams.append('requestid', requestId);

    let formData:FormData = new FormData();
    formData.append('email', email);
    formData.append('request', request);
    formData.append('requestid', requestId);




    return this.http.post(this.baseUrl+"/delete-request",formData,{headers: this.getHeaders()})
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
