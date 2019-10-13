import { Component } from '@angular/core';
import { NgUploaderOptions } from 'ngx-uploader';

@Component({
  selector: 'initial-config',
  styleUrls: ['./initialConfig.scss'],
  templateUrl: './initialConfig.html',
})
export class InitialConfig {

  numberOfLevel: number;
  securityLvl: String[];
  selectLvl: String;
  bindResult: any;

  public fileUploaderOptions: NgUploaderOptions = {
    // url: 'http://website.com/upload'
    url: '',
  };

  constructor() {
  } 
}
