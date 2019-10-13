/**
 * Created by EMS on 6/1/2017.
 */
import { Routes, RouterModule }  from '@angular/router';

import { Document } from './document.component';

// noinspection TypeScriptValidateTypes
const routes: Routes = [
  {
    path: '',
    component: Document
  }
];

export const routing = RouterModule.forChild(routes);
