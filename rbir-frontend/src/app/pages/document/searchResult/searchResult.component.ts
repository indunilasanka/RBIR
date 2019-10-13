import {Component, Input} from '@angular/core';
import {layoutPaths} from '../../../theme';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import {RequestModel} from '../../../models/request.model';
import { UserSelectModel } from '../userSelectModel/userSelectModel.component';

@Component({
  selector: 'search-result',
  templateUrl: './searchResult.html',
  styleUrls: ['./searchResult.scss']
})
export class SearchResult {

  path:String = layoutPaths.images.docIcons;
  @Input() searchResults:Array<Object>;
  @Input() reqId:string;


  constructor(private modalService: NgbModal ) {
  }

  ngOnInit() {
  }

  expandMessage (message){
    message.expanded = !message.expanded;
  }

  test() {
    console.log("test clicked" ,this.searchResults);
  }

  openmodel(docId:string,securityLevel:string){
        const activeModal = this.modalService.open(UserSelectModel, {size: 'sm',
                                                              backdrop: 'static'});
    activeModal.componentInstance.modalHeader = 'Static modal';
    activeModal.componentInstance.modalContent = `This is static modal, backdrop click
                                                    will not close it. Click × or confirmation button to close modal.`;
    activeModal.componentInstance.docId = docId;
    activeModal.componentInstance.securityLevel =securityLevel;
    activeModal.componentInstance.reqId = this.reqId;
 }


}
