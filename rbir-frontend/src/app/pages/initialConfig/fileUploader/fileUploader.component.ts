import { Component, ViewChild, Input, Output, EventEmitter, ElementRef, Renderer , OnInit } from '@angular/core';

import { FileUploadService } from './fileUpload.service';
import { Ng2Bs3ModalModule } from 'ng2-bs3-modal/ng2-bs3-modal';
import { DocumentModel } from '../../../models/document.model';

@Component({
  selector: 'file-uploader-com',
  styleUrls: ['./fileUploader.scss'],
  templateUrl: './fileUploader.html',
})
export class FileUploader {

  @ViewChild('childComponent2') childComponent;
  @ViewChild('fileUpload') public _fileUpload: ElementRef;
  @ViewChild('singleFileUpload') public _singleFileUpload: ElementRef;
  @ViewChild('inputText') public _inputText: ElementRef;
  @ViewChild('modal') public _model: ElementRef;

  numberOfLevel: number;
  securityLvls: string[];
  selectedLvl: string;
  data: any = null;
  fileName: string = '';
  popupTitle: String = '';
  popupMessage: String = '';
  documents: DocumentModel[] = [];
  fileList: File[] = null;
  selectSingleFile: boolean = false;
  submited: boolean = false;
  
  result: any;
  accuracy: number[];

  uploadFileInProgress: boolean;
  
  constructor(private renderer: Renderer, private fileUploadService: FileUploadService) {
   }

  setSecurityLevel(numberOfLevels: number) {
    
    this.numberOfLevel = numberOfLevels;
    this.securityLvls = [];
    for (let i = 1; i <= this.numberOfLevel; i++) {
      this.securityLvls.push('security_level_' + i);
    }
    if (numberOfLevels) {
      this.selectedLvl = 'security_level_1';
    } 
   
  }

  setSelectedLvl(lvl: string) {
    this.selectedLvl = lvl;
  }

  bringFileSelector(): boolean {
    if (this.selectSingleFile) {
      this.renderer.invokeElementMethod(this._singleFileUpload.nativeElement, 'click');
    }else {
      this.renderer.invokeElementMethod(this._fileUpload.nativeElement, 'click');
    }
    return false;
  }

  beforeFileUpload($event) {
    let files;
    if (this.selectSingleFile) {
      files = this._singleFileUpload.nativeElement.files;
    }else {
      files = this._fileUpload.nativeElement.files;
    }
    if (files.length) {
      const fileCount = files.length;
      if (fileCount > 0) {
        
        const firstFile: File = files[0];
        this.fileName = firstFile.webkitRelativePath;
        const fileNamepart: string[] = this.fileName.split('/');
        this.fileName = fileNamepart[0];

        for (let i = 0; i < fileCount; i++) {
          const fielType: String = files.item(i).type;
          if (fielType.includes('pdf') || fielType.includes('officedocument.word')) {
            const document: DocumentModel = new DocumentModel();
            document.securityLevel = this.selectedLvl;
            document.file = files.item(i);
            this.documents.push(document);
          }
        }
      }
    }
  }


  removeDocument(document: DocumentModel) {
    this.documents = this.documents.filter(item => item !== document);
  }

  startFileupload() {
    
    if (this.documents.length !== 0) {
      this.submited = true;
      this.fileUploadService.uploadFolder(this.documents, this.securityLvls).subscribe(

        data => { 
          this.data = data;
          this.result = JSON.parse(this.data);
          console.log("Indexed result -----------------", this.data);
          
          // if (this.data.success) {
            this.childComponent.doSomething(this.result);
          // }
            
          // if (this.data.success == true) {
          //   console.log("Indexed result ----------------- sucssse");
          //   // this.popUp('Success', 'File Successfully Indexed!');
          //   // console.log("Popup closed -----------------",data);
          //   // this.childComponent.doSomething(data);  
          // } else {
          //   console.log("Indexed result ----------------- else");
          //   // this.popUp('Fail', 'File Indexing Failed!');
          // }
        },
        error => {
          this.submited = false;
          this.data = error.toString();
          console.log("Indexed result -----------------error", this.data);
          this.popUp('Fail', this.data.toString());
        },
      );

    }
  }

  // refreshFileupload(){
  //   this.childComponent.doSomething(this.result);  
  // }

  popUp(title: String, message: String) {
    console.log("test");
    this.popupTitle = title;
    this.popupMessage = message;
    jQuery(this._model).trigger("open");
  }
}

