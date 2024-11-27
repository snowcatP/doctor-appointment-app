import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PatientTableComponent } from './patient-table/patient-table.component';

const routes: Routes = [
  {
    path: '',
    component: PatientTableComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PatientRoutingModule { }
