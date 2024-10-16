import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { authGuard } from './guard/auth.guard';
import { HomeComponent } from './client/master-layout/home/home.component';
import { RegisterComponent } from './client/master-layout/register/register.component';
import { LoginComponent } from './client/master-layout/login/login.component';

const routes: Routes = [
  {
    path: '',
    // loadComponent: () => import("./client/master-layout/home/home.component").then(m => m.HomeComponent),
    component: HomeComponent,
    title: 'Online Doctor Booking - Schedule Medical Appointments in Minutes'
},
{
    path: 'login',
    // loadChildren: () => import("./component/login/login.component").then(m => m.LoginComponent),
    component: LoginComponent,
    title: 'Login',
},
{
    path: 'register',
    // loadChildren: () => import("./component/register/register.component").then(m => m.RegisterComponent),
    component: RegisterComponent,
    title: 'Register',
    // canActivate: [authGuard]
},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
