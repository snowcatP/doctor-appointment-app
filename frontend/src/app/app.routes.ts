import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './home/master-layout/login/login.component';

export const routes: Routes = [
    {
        path: '',
        component: HomeComponent,
        title: 'Online Doctor Booking – Schedule Medical Appointments in Minutes'
    },
    {
        path: 'login',
        component: LoginComponent,
        title: 'Login'
    },

];
