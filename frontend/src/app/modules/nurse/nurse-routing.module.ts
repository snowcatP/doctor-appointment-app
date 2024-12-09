import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NurseChangePasswordComponent } from './pages/nurse-change-password/nurse-change-password.component';
import { NurseDashboardComponent } from './pages/nurse-dashboard/nurse-dashboard.component';
import { NurseProfileComponent } from './pages/nurse-profile/nurse-profile.component';
import { NurseComponent } from './nurse.component';
import { CreateMedicalRecordComponent } from './pages/create-medical-record/create-medical-record.component';
import { EditMedicalRecordComponent } from './pages/edit-medical-record/edit-medical-record.component';
const routes: Routes = [
  {
    // doctor
    path: '',
    component: NurseComponent,
    children: [
      {
        path: '',
        redirectTo: '/nurse/dashboard',
        pathMatch: 'full'
      },
      {
        path: 'dashboard',
        component: NurseDashboardComponent,
        title: 'Nurse Dashboard',
      },
      {
        path: 'profile',
        component: NurseProfileComponent,
        title: 'Nurse Profile',
      },
      {
        path: 'change-password',
        component: NurseChangePasswordComponent,
        title: 'Nurse Change Password'
      },
      {
        path: 'create-medical-record/:id',
        component: CreateMedicalRecordComponent,
        title: 'Nurse Create Medical Record'
      },
      {
        path: 'edit-medical-record/:id',
        component: EditMedicalRecordComponent,
        title: 'Nurse Edit Medical Record'
      },

    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class NurseRoutingModule { }
