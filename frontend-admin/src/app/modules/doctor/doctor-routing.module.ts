import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DoctorTableComponent } from './doctor-table/doctor-table.component';

const routes: Routes = [
  {
    path:'',
    component: DoctorTableComponent

  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DoctorRoutingModule { }
