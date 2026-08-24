import { Routes } from '@angular/router';

import { Login } from './login/login';
import { CadastroComponent } from './cadastro/cadastro';
import { Dashboard } from './dashboard/dashboard';
import { Simulador } from './simulador/simulador';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: Login },
  { path: 'cadastro', component: CadastroComponent },
  { path: 'dashboard', component: Dashboard },
  { path: 'simulador', component: Simulador }
];