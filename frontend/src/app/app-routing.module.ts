import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { authGuard } from './guard/auth.guard';
import { HomeComponent } from './client/master-layout/home/home.component';
import { RegisterComponent } from './client/master-layout/register/register.component';
import { LoginComponent } from './client/master-layout/login/login.component';
import { SearchDoctorComponent } from './client/master-layout/search-doctor/search-doctor.component';
import { DoctorProfileComponent } from './client/master-layout/doctor-profile/doctor-profile.component';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    title: 'Online Doctor Booking - Schedule Medical Appointments in Minutes',
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
    // canActivate: [authGuard]
  },
  {
    path: 'search-doctor',
    component: SearchDoctorComponent,
    title: 'Search Doctor',
  },
  {
    path: 'doctor-profile',
    component: DoctorProfileComponent,
    title: 'Doctor Profile',
  },
  {
    path: 'patient',
    loadChildren: () =>
      import('./client/patient/patient.module').then((m) => m.PatientModule),
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
