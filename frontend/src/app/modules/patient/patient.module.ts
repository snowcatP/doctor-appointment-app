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
import { DateFormatPipeModule } from '../../core/pipes/date-format.module';
import { FormsModule } from '@angular/forms';
import { CalendarModule } from 'primeng/calendar';
import { PaginatorModule } from 'primeng/paginator';
import { DialogModule } from 'primeng/dialog';
import { MatDialogModule } from '@angular/material/dialog';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { SharedModule } from "../../shared/shared.module";
import { MatButtonModule } from '@angular/material/button';
import { DropdownModule } from 'primeng/dropdown';
import { ScrollPanelModule } from 'primeng/scrollpanel';
import { ReactiveFormsModule } from '@angular/forms';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { PasswordModule } from 'primeng/password';
import { DividerModule } from 'primeng/divider';
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
    DateFormatPipeModule,
    FormsModule,
    CalendarModule,
    PaginatorModule,
    DialogModule,
    MatDialogModule,
    ProgressSpinnerModule,
    SharedModule,
    MatButtonModule,
    DropdownModule,
    ScrollPanelModule,
    ReactiveFormsModule,
    InputGroupModule,
    InputGroupAddonModule,
    PasswordModule,
    DividerModule
]
})
export class PatientModule { }
