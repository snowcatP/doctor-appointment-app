import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PatientTableComponent } from './patient-table/patient-table.component';
import { PatientEditComponent } from './patient-edit/patient-edit.component';
import { PatientDashboardComponent } from './patient-dashboard/patient-dashboard.component';

const routes: Routes = [
  {
    path: '',
    component: PatientTableComponent
  },
  {
    path: 'dashboard',
    component: PatientDashboardComponent
  },
  {
    path: 'edit-patient',
    component: PatientEditComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PatientRoutingModule { }
