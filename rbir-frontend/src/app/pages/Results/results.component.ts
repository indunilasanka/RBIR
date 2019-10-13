import { Component, Input,ViewChild } from '@angular/core';


@Component({
  selector: 'results',
  templateUrl: './results.html',
  styleUrls: ['./results.scss']
})

export class Results {

     @ViewChild('childComponent2') childComponent2;
   


     refreshResults(){
          this.childComponent2.loadResults();

     }

}
