import {Injectable} from '@angular/core';
import { Http,RequestOptions, Response,Headers} from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
import { environment } from '../../../environments/environment';

@Injectable()
export class DocumentService {

  private host: string = environment.host;
  private port: string = environment.port;
  private endcall: string = '/documents';
   private checked: string ;
    
  private baseUrl: string = this.host +':'+ this.port + this.endcall;

  constructor(private http : Http) {
  }

  private getHeaders(){
    // I included these headers because otherwise FireFox
    // will request text/html instead of application/json
    let headers = new Headers();
    headers.append('Accept', 'application/json');
    return headers;
  }


  getDocuments(query : string,isChecked:boolean): Observable<Object[]> {

    // console.log(this.http.get(this.baseUrl+'/list', {headers: this.getHeaders()})
    //   .map(response => response.json())
    //   .catch(this.handleError));

    if(isChecked){
         this.checked = "true";
    }else{
         this.checked = "false";
    }

    let formData:FormData = new FormData();
    formData.append('query', query);
    formData.append('checked', this.checked);
	  formData.append('level', "_all");
    let headers = new Headers();
    headers.append('Accept', 'application/json');
    headers.append('Access-Control-Allow-Origin', '*');

    return this.http.post(this.baseUrl+'/list',formData,{headers: this.getHeaders()})
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
