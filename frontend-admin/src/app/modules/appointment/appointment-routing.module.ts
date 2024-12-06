import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AppointmentTableComponent } from './appointment-table/appointment-table.component';

const routes: Routes = [
  {
    path:'',
    component: AppointmentTableComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AppointmentRoutingModule { }
