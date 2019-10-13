import { Component, Input ,ViewChild } from '@angular/core';
import { DocumentModel } from '../../models/document.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { RequestModel } from '../../models/request.model';
import { DocumentService } from './document.service';
import { CreateUser } from '../createUser/createUser.component';
import { UserSelectModel } from './userSelectModel/userSelectModel.component';
import {RequestService} from '../document/request/request.service';

@Component({
  selector: 'document',
  templateUrl: './document.html',
  styleUrls: ['./document.scss']
})

export class Document {

  data: Array<Object> = null;
  searchResults: Array<DocumentModel>;
  doc1: DocumentModel;
  doc2: DocumentModel;
  request: RequestModel = null;
  quary: string = '';
  reqId: string = '';
  isChecked: boolean = false;
  @ViewChild('childComponent') childComponent;


  constructor(private documentServece: DocumentService, private modalService: NgbModal,private _requestService: RequestService) {
  }

  ngOnInit() {
  }

 searchDocuments(searchQuary: string) {
    console.log("searchDocuments()", searchQuary);
    this.documentServece.getDocuments(searchQuary, this.isChecked).subscribe(
      data => {
        this.data = data;
        this.loadDocuments();
      },
      error => console.log(error)
    );
  }

  refreshRequest(){
    this.childComponent.loadFeed();
  }

  loadDocuments() {
    this.searchResults = new Array<DocumentModel>();
    for (let entry of this.data) {
      console.log(entry);
      let doc:DocumentModel = new DocumentModel;
      doc.id = entry["id"];
      doc.title = entry["title"];
      doc.category = entry["category"];
      doc.summary = entry["summary"];
      doc.securityLevel = entry["securityLevel"];
      this.searchResults.push(doc);
    }
    console.log(this.data);
  }

  someMethod(event: string) {
   
    this.quary = event;
    console.log("query = ",event);
  }

  someMethod2(event: string) {
   
    this.reqId = event;
    console.log("req Id = ",event);
  }

  click() {
    console.log("click.........");
    
    const activeModal = this.modalService.open(UserSelectModel, { size: 'sm', backdrop: 'static' }); 
    activeModal.componentInstance.modalHeader = 'Static modal';
    activeModal.componentInstance.modalContent = `This is static modal, backdrop click 
    will not close it. Click × or confirmation button to close modal.`;
  }
}
