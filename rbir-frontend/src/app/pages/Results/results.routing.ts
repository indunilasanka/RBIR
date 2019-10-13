import { Routes, RouterModule } from '@angular/router';

import { Results } from './results.component';

// noinspection TypeScriptValidateTypes
const routes: Routes = [
  {
    path: '',
    component: Results
  }
];

export const routing = RouterModule.forChild(routes);
