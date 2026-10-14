import { Routes } from '@angular/router';
import { authGuard } from './core/auth.guard';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'login' },
  { path: 'login', loadComponent: () => import('./pages/login/login.component').then((m) => m.LoginComponent) },
  {
    path: 'clientes',
    canActivate: [authGuard],
    loadComponent: () => import('./pages/clientes/clientes.component').then((m) => m.ClientesComponent)
  },
  {
    path: 'paquetes',
    canActivate: [authGuard],
    loadComponent: () => import('./pages/paquetes/paquetes.component').then((m) => m.PaquetesComponent)
  },
  { path: '**', redirectTo: 'login' }
];
