import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PatientRoutingModule } from './patient-routing.module';
import { PatientDashboardComponent } from './patient-dashboard/patient-dashboard.component';
import { PatientProfileComponent } from './patient-profile/patient-profile.component';
import { PatientChangePasswordComponent } from './patient-change-password/patient-change-password.component';
import { PatientBreadCrumbComponent } from './patient-bread-crumb/patient-bread-crumb.component';
import { RouterModule } from '@angular/router';
import { PatientComponent } from './patient.component';
import { PatientSidebarComponent } from './patient-sidebar/patient-sidebar.component';
import { MatTabsModule } from '@angular/material/tabs';
import { ImageModule } from 'primeng/image';

@NgModule({
  declarations: [
    PatientComponent,
    PatientDashboardComponent,
    PatientProfileComponent,
    PatientChangePasswordComponent,
    PatientBreadCrumbComponent,
    PatientSidebarComponent,
  ],
  imports: [
    CommonModule,
    PatientRoutingModule,
    RouterModule,
    MatTabsModule,
    ImageModule,
  ],
})
export class PatientModule {}
