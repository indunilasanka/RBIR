import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgaModule } from '../../theme/nga.module';
import { FormsModule as AngularFormsModule } from '@angular/forms';

import { routing } from './results.routing';
import { Results } from './results.component';
import {ConfirmResults} from './confirmResults/confirmResults.component';

import {ConfirmResultsService} from './confirmResults/confirmResults.service';


@NgModule({
  imports: [
    CommonModule,
    NgaModule,
    routing,
    AngularFormsModule,
  ],
  declarations: [
    Results,
    ConfirmResults
  ],
  providers: [
    ConfirmResultsService,
  ],

})

export class ResultsModule {
}
