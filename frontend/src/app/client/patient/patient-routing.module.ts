import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PatientComponent } from './patient.component';
import { PatientDashboardComponent } from './patient-dashboard/patient-dashboard.component';
import { PatientProfileComponent } from './patient-profile/patient-profile.component';

const routes: Routes = [
  {
    path: '',
    component: PatientComponent,
    children: [
      {
        path: '',
        component: PatientDashboardComponent,
        title: 'Patient Dashboard',
      },
      {
        path: 'profile',
        component: PatientProfileComponent,
        title: 'Patient Profile',
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PatientRoutingModule {}
