import { Routes, RouterModule } from '@angular/router';
import { ModuleWithProviders } from '@angular/core';
import { AuthGuard } from './guards/auth.guard';
import {Pages} from './pages/pages.component';
import {Login} from './pages/login/login.component';

export const routes: Routes = [];

export const routing: ModuleWithProviders = RouterModule.forRoot(routes, { useHash: true });
