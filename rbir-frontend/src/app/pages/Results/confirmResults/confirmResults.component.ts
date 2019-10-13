import {Component, Output, EventEmitter} from '@angular/core';
import {ConfirmResultsService} from './confirmResults.service';
import { DocumentModel } from '../../../models/document.model';
import {layoutPaths} from '../../../theme';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'confirmresults',
  templateUrl: './confirmResults.html',
  styleUrls: ['./confirmResults.scss']
})
export class ConfirmResults {
  path:String = layoutPaths.images.profile+"doc.jpg";
  data: Array<Object> = null;
  searchResults: Array<DocumentModel>;
  searchId:string;

  constructor(private confirmResultsService: ConfirmResultsService) {
  }

 ngOnInit() {
      this.loadResults();
  }

  loadResults(){
    this.confirmResultsService.getResults().subscribe(
      data => {
        console.log(data);
        this.data = data;
        this.loadDocuments();
      },
      error => console.log(error)
    );
  }

  loadDocuments() {
    this.searchResults = new Array<DocumentModel>();
    for (let entry of this.data) {

      this.searchId = entry["id"];
      console.log(entry["document"].id);
      let doc:DocumentModel = new DocumentModel;
      doc.id = entry["document"].id;
      doc.title = entry["document"].title;
      doc.category = entry["document"].category;
      doc.summary = entry["document"].summary;
      doc.securityLevel = entry["document"].securityLevel;
      doc.filePath = entry["document"].file;
      this.searchResults.push(doc);
    }
    console.log(this.data);
  }

  confirm(filePath:string){
    this.confirmResultsService.confirmRequest(this.searchId,filePath).subscribe(
      data => {
        console.log(data);
      },
      error => console.log(error)
    );
  }

    cancel(){
    this.confirmResultsService.cancelRequest(this.searchId).subscribe(
      data => {
        console.log(data);
      },
      error => console.log(error)
    );
  }

    reject(){
    this.confirmResultsService.rejectRequest(this.searchId).subscribe(
      data => {
        console.log(data);
      },
      error => console.log(error)
    );
  }

  expandMessage (message){
    message.expanded = !message.expanded;
  }
}

