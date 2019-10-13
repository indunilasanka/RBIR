/**
 * Created by EMS on 6/1/2017.
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgaModule } from '../../theme/nga.module';
import { FormsModule as AngularFormsModule } from '@angular/forms';
import { NgbDropdownModule, NgbModalModule } from '@ng-bootstrap/ng-bootstrap';

import { routing } from './document.routing';
import { Document } from './document.component';
import { DocumentService } from './document.service';
import {SearchResult} from './searchResult/searchResult.component';
import {Request} from './request/request.component';
import {RequestService} from './request/request.service';
import { UserSelectModel } from './userSelectModel/userSelectModel.component';
import {UserSelectModelService} from './userSelectModel/userSelectModel.service';

@NgModule({
  imports: [
    CommonModule,
    NgaModule,
    routing,
    AngularFormsModule,
    NgbDropdownModule,
    NgbModalModule
  ],
  declarations: [
    Document,
    SearchResult,
    Request,
    UserSelectModel,
  ],
  entryComponents: [
    UserSelectModel,
  ],
  providers: [
    DocumentService,
    RequestService,
    UserSelectModelService,
  ],

})
export class DocumentModule {
}
