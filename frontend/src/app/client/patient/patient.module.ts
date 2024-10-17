import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PatientRoutingModule } from './patient-routing.module';
import { PatientDashboardComponent } from './patient-dashboard/patient-dashboard.component';
import { PatientProfileComponent } from './patient-profile/patient-profile.component';


@NgModule({
  declarations: [
    PatientDashboardComponent,
    PatientProfileComponent
  ],
  imports: [
    CommonModule,
    PatientRoutingModule
  ]
})
export class PatientModule { }
