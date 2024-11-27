import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PatientTableComponent } from './patient-table/patient-table.component';
import { PatientEditComponent } from './patient-edit/patient-edit.component';

const routes: Routes = [
  {
    path: '',
    component: PatientTableComponent
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
