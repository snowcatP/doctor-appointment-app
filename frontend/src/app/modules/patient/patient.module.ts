import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PatientRoutingModule } from './patient-routing.module';
import { RouterModule } from '@angular/router';
import { MatTabsModule } from '@angular/material/tabs';
import { ImageModule } from 'primeng/image';
import { PatientComponent } from './patient.component';
import { PatientSidebarComponent } from './pages/patient-sidebar/patient-sidebar.component';
import { PatientProfileComponent } from './pages/patient-profile/patient-profile.component';
import { PatientDashboardComponent } from './pages/patient-dashboard/patient-dashboard.component';
import { PatientBreadCrumbComponent } from './pages/patient-bread-crumb/patient-bread-crumb.component';
import { PatientChangePasswordComponent } from './pages/patient-change-password/patient-change-password.component';


@NgModule({
  declarations: [
    PatientComponent,
    PatientSidebarComponent,
    PatientProfileComponent,
    PatientDashboardComponent,
    PatientBreadCrumbComponent,
    PatientChangePasswordComponent
  ],
  imports: [
    CommonModule,
    PatientRoutingModule,
    RouterModule,
    MatTabsModule,
    ImageModule,
  ]
})
export class PatientModule { }
