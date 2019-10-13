import {Component} from '@angular/core';
import {DocumentModel} from '../../models/document.model';

import {DocumentService} from './document.service';

@Component({
  selector: 'document',
  templateUrl: './document.html',
  styleUrls: ['./document.scss']
})

export class Document {

  data: Array<Object> = null;
  searchResults : Array<DocumentModel>;
  doc1 : DocumentModel;
  doc2 : DocumentModel;

  constructor(private documentServece: DocumentService) {
  }

  ngOnInit() {

  }

  searchDocuments(searchQuary : string){
    console.log("searchDocuments()", searchQuary);
    this.documentServece.getDocuments(searchQuary).subscribe(
      data => {
        this.data = data;
        this.loadDocuments();
      },
      error => console.log(error)
    );
  }

  loadDocuments(){

    this.searchResults = new Array<DocumentModel>();
    for (let entry of this.data) {
      console.log(entry);
      let doc:DocumentModel = new DocumentModel;
      doc.title = entry["title"];
      doc.category = entry["category"];
      doc.summary = entry["summary"];
      this.searchResults.push(doc);
    }
    console.log(this.data);


  }
}
