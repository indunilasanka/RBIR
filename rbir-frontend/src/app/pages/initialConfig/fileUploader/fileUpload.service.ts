/**
 * Created by EMS on 5/31/2017.
 */
import { Injectable,Component } from '@angular/core';
import { Http,RequestOptions, Response,Headers} from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
import { DocumentModel } from '../../../models/document.model';
import {element} from "protractor";

import { environment } from '../../../../environments/environment';


@Injectable()
export class FileUploadService {

  private host: string = environment.host;
  private port: string = environment.port;
  private endcall: string = '/documents';
    
  private baseUrl: string = this.host +':'+ this.port + this.endcall;

  constructor(private http : Http){
  }


  private getHeaders(){
    // I included these headers because otherwise FireFox
    // will request text/html instead of application/json
    let headers = new Headers();
    headers.append('Access-Control-Allow-Origin', '*');
    return headers;
  }

  getDocuments(): Observable<Object> {

    console.log(this.http.get(this.baseUrl+'/list', {headers: this.getHeaders()})
      .map(response => response.json())
      .catch(this.handleError));

    return this.http.get(this.baseUrl+'/list',{headers: this.getHeaders()})
      .map(response => response)
      .catch(this.handleError);
  }

  uploadFiles(file: File): Observable<Object[]> {
    console.log("came for uploading3");
    let formData:FormData = new FormData();
    formData.append('file', file, file.name);

    let headers = new Headers();
    headers.append('Content-Type', 'multipart/form-data');
    headers.append('Accept', 'application/json');
    headers.append('Access-Control-Allow-Origin', '*');

    return this.http.post(this.baseUrl+'/upload', formData, {headers: this.getHeaders()})
      .map(response => response.text())
      .catch(this.handleError);

  }


  uploadFolder(files: DocumentModel[], securityLvls: string[]): Observable<Object[]> {
    const formData: FormData = new FormData();
    const fileCount: number = files.length;
    if (fileCount > 0) {
      files.forEach(element => {
        formData.append('file', element.file );
        formData.append('level', element.securityLevel );
      });

      securityLvls.forEach(element => {
        formData.append('securitylvls', element );
      });

      const headers = new Headers();
      headers.append('Content-Type', undefined);
      headers.append('Accept', 'application/json');
      headers.append('Access-Control-Allow-Origin', '*');

      return this.http.post(this.baseUrl + '/setup', formData, { headers: this.getHeaders() })
        .map(response => response.text())
        .catch(this.handleError);
    }
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
