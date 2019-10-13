import {Component} from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import {GlobalState} from '../../../global.state';


import { UserService } from '../../../services/user.service';

import { ChangePassward } from '../../../pages/changePassward/changePassward.component';

@Component({
  selector: 'ba-page-top',
  templateUrl: './baPageTop.html',
  styleUrls: ['./baPageTop.scss']
})
export class BaPageTop {


  data: Array<Object> = null;

  ngOnInit() {
      this.userService.getUser().subscribe(
      data => {
        console.log(data);
        this.data = data;
      },
      error => console.log(error)
    );
  }

  public isScrolled:boolean = false;
  public isMenuCollapsed:boolean = false;

  constructor(private _state:GlobalState,private userService: UserService,private modalService: NgbModal) {
    this._state.subscribe('menu.isCollapsed', (isCollapsed) => {
      this.isMenuCollapsed = isCollapsed;
    });
  }

  public toggleMenu() {
    this.isMenuCollapsed = !this.isMenuCollapsed;
    this._state.notifyDataChanged('menu.isCollapsed', this.isMenuCollapsed);
    return false;
  }

  public scrolledChanged(isScrolled) {
    this.isScrolled = isScrolled;
  }

  changePassward() {
    console.log("clicked....");
    
    const activeModal = this.modalService.open(ChangePassward, { size: 'sm', backdrop: 'static' }); 
    activeModal.componentInstance.modalHeader = 'Static modal';
    activeModal.componentInstance.modalContent = `This is static modal, backdrop click 
    will not close it. Click × or confirmation button to close modal.`;
  }
}
