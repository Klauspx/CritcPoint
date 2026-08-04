import { Routes } from '@angular/router';

import { Login } from './login/login'; 

import { CadastroComponent } from './cadastro/cadastro'; 

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: Login },
  { path: 'cadastro', component: CadastroComponent }
];