import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DoctorTableComponent } from './doctor-table/doctor-table.component';
import { DoctorAddNewComponent } from './doctor-add-new/doctor-add-new.component';
import { DoctorEditComponent } from './doctor-edit/doctor-edit.component';

const routes: Routes = [
  {
    path:'',
    component: DoctorTableComponent
  },
  {
    path:'add-doctor',
    component: DoctorAddNewComponent
  },
  {
    path:'edit-doctor',
    component: DoctorEditComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DoctorRoutingModule { }
