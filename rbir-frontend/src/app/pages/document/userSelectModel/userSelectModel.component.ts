import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import {UserSelectModelService} from './userSelectModel.service';

@Component({
  selector: 'user-selectModel',
  styleUrls: [('./userSelectModel.scss')],
  templateUrl: './userSelectModel.html'
})

export class UserSelectModel implements OnInit {

  
  data: Array<Object> = null;
  modalHeader: string;
  modalContent: string = `Lorem ipsum dolor sit amet,
   consectetuer adipiscing elit, sed diam nonummy
   nibh euismod tincidunt ut laoreet dolore magna aliquam
   erat volutpat. Ut wisi enim ad minim veniam, quis
   nostrud exerci tation ullamcorper suscipit lobortis
   nisl ut aliquip ex ea commodo consequat.`;
   docId:string;
   reqId:string;
   securityLevel:string;


  constructor(private activeModal: NgbActiveModal,private userSelectModelService: UserSelectModelService) {
  }

  ngOnInit() {


    this.userSelectModelService.getHighEndUsers(this.securityLevel).subscribe(
         data => {
              console.log(data);
              this.data = data;
         },
         error => console.log(error)
    );

  }

  closeModal() {
    this.activeModal.close();
  }

  sendResultForConfirmation(username:string){
    console.log("submitting result for confirmation");
    this.activeModal.close();
    this.userSelectModelService.passResultToHighEndUser(username, this.reqId,this.docId,this.securityLevel).subscribe(
         data => {
              console.log(data);
         },
         error => console.log(error)
    );

  }
}
