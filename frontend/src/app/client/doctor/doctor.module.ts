import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DoctorRoutingModule } from './doctor-routing.module';
import { DoctorProfileComponent } from './doctor-profile/doctor-profile.component';
import { DoctorBreadCrumbComponent } from './doctor-bread-crumb/doctor-bread-crumb.component';
import { DoctorSidebarComponent } from './doctor-sidebar/doctor-sidebar.component';
import { DoctorComponent } from './doctor.component';
import { DoctorScheduleComponent } from './doctor-schedule/doctor-schedule.component';
import { DoctorCalendarComponent } from './doctor-calendar/doctor-calendar.component';
import { FullCalendarModule } from '@fullcalendar/angular';
import { DoctorAppointmentComponent } from './doctor-appointment/doctor-appointment.component';


@NgModule({
  declarations: [
    DoctorComponent,
    DoctorProfileComponent,
    DoctorBreadCrumbComponent,
    DoctorSidebarComponent,
    DoctorScheduleComponent,
    DoctorCalendarComponent,
    DoctorAppointmentComponent
  ],
  imports: [
    CommonModule,
    DoctorRoutingModule,
    FullCalendarModule
  ]
})
export class DoctorModule { }
