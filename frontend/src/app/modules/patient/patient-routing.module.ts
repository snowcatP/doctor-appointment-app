import { NgModule, Input } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PatientDashboardComponent } from './pages/patient-dashboard/patient-dashboard.component';
import { PatientProfileComponent } from './pages/patient-profile/patient-profile.component';
import { PatientComponent } from './patient.component';
import { PatientChangePasswordComponent } from './pages/patient-change-password/patient-change-password.component';

const routes: Routes = [
  {
    // patient
    path: '',
    component: PatientComponent,
    children: [
      {
        path: '',
        redirectTo: '/patient/dashboard',
        pathMatch: 'full'
      },
      {
        path: 'dashboard',
        component: PatientDashboardComponent,
        title: 'Patient Dashboard',
      },
      {
        path: 'profile',
        component: PatientProfileComponent,
        title: 'Patient Profile'
      },
      {
        path: 'change-password',
        component: PatientChangePasswordComponent,
        title: 'Patient Change Password',
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PatientRoutingModule { }
