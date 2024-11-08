import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DoctorRoutingModule } from './doctor-routing.module';
import { DoctorAppointmentComponent } from './pages/doctor-appointment/doctor-appointment.component';
import { DoctorBreadCrumbComponent } from './pages/doctor-bread-crumb/doctor-bread-crumb.component';
import { DoctorCalendarComponent } from './pages/doctor-calendar/doctor-calendar.component';
import { DoctorDashboardComponent } from './pages/doctor-dashboard/doctor-dashboard.component';
import { DoctorProfileComponent } from './pages/doctor-profile/doctor-profile.component';
import { DoctorScheduleComponent } from './pages/doctor-schedule/doctor-schedule.component';
import { DoctorSidebarComponent } from './pages/doctor-sidebar/doctor-sidebar.component';
import { DoctorComponent } from './doctor.component';
import { FullCalendarModule } from '@fullcalendar/angular';
import { DoctorReviewComponent } from './pages/doctor-review/doctor-review.component';
import { ReplyDialogComponent } from './pages/doctor-review/reply-dialog/reply-dialog.component';
import { FormsModule } from '@angular/forms';
import { MatDialogModule } from '@angular/material/dialog';
import { PaginatorModule } from 'primeng/paginator';
import { DoctorMypatientComponent } from './pages/doctor-mypatient/doctor-mypatient.component';
@NgModule({
  declarations: [
    DoctorAppointmentComponent,
    DoctorBreadCrumbComponent,
    DoctorCalendarComponent,
    DoctorDashboardComponent,
    DoctorProfileComponent,
    DoctorScheduleComponent,
    DoctorSidebarComponent,
    DoctorComponent,
    DoctorReviewComponent,
    ReplyDialogComponent,
    DoctorMypatientComponent,
  ],
  imports: [
    CommonModule,
    DoctorRoutingModule,
    FullCalendarModule,
    FormsModule,
    MatDialogModule,
    PaginatorModule
  ]
})
export class DoctorModule { }
