import { Routes } from '@angular/router';
import { HomeComponent } from './client/master-layout/home/home.component';
import { LoginComponent } from './component/login/login.component';
import { RegisterComponent } from './component/register/register.component';
import { authGuardGuard } from './auth-guard.guard';

export const routes: Routes = [
    {
        path: '',
        component: HomeComponent,
        title: 'Online Doctor Booking - Schedule Medical Appointments in Minutes'
    },
    {
        path: 'login',
        component: LoginComponent,
        title: 'Login',
    },
    {
        path: 'register',
        component: RegisterComponent,
        title: 'Register',
        canActivate: [authGuardGuard]
    },


];

