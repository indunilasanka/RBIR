import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgaModule } from '../../theme/nga.module';

import { CreateUser } from './createUser.component';
import { routing } from './createUser.routing';
import { NgbDropdownModule, NgbModalModule } from '@ng-bootstrap/ng-bootstrap';
import { DefaultModal } from '../ui/components/modals/default-modal/default-modal.component';
import { CreateUserService } from './createUser.service';

@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    NgaModule,
    routing,
    NgbDropdownModule,
    NgbModalModule,
  ],
  declarations: [
    CreateUser,
    DefaultModal,
  ],
  entryComponents: [
    DefaultModal,
  ],
  providers: [
    CreateUserService,
  ],
})
export class CreateUserModule {}
