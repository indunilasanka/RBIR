/**
 * Created by EMS on 6/1/2017.
 */
import { Routes, RouterModule } from '@angular/router';

import { CreateUser } from './createUser.component';

// noinspection TypeScriptValidateTypes
const routes: Routes = [
  {
    path: '',
    component: CreateUser,
  },
];

export const routing = RouterModule.forChild(routes);
