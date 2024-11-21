import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './shared/home/home.component';
import { authGuard } from './core/guards/auth.guard';

const routes: Routes = [
  {
    path: 'auth',
    title: 'Admin Page - Doctor Booking Management',
    loadChildren: () =>
      import('./modules/authentication/authentication.module').then((m) => m.AuthenticationModule)
  },
  {
    path: '',
    component: HomeComponent,
    canActivate: [authGuard],
    title: 'Admin Page - Doctor Booking Management',
    children: [
      {
        path: '',
        redirectTo: '/dashboard',
        pathMatch: 'full',
      },
      {
        path: 'dashboard',
        loadChildren: () =>
          import('./shared/home-component/home.routes').then((m) => m.HomeRoute),
      },
      {
        path: 'doctor',
        loadChildren: () =>
          import('./modules/doctor/doctor.module').then((m)=> m.DoctorModule)
      },
      {
        path: 'specialty',
        loadChildren: () =>
          import('./modules/specialty/specialty.module').then((m)=> m.SpecialtyModule)
      }
    ],
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
