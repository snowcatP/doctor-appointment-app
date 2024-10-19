import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DoctorComponent } from './doctor.component';
import { DoctorDashboardComponent } from './doctor-dashboard/doctor-dashboard.component';
import { DoctorProfileComponent } from './doctor-profile/doctor-profile.component';
import { DoctorScheduleComponent } from './doctor-schedule/doctor-schedule.component';
import { DoctorCalendarComponent } from './doctor-calendar/doctor-calendar.component';
import { DoctorAppointmentComponent } from './doctor-appointment/doctor-appointment.component';

const routes: Routes = [
  {
    // doctor
    path: '',
    component: DoctorComponent,
    children: [
      {
        path: '',
        component: DoctorDashboardComponent,
        title: 'Doctor Dashboard',
      },
      {
        path: 'profile',
        component: DoctorProfileComponent,
        title: 'Doctor Profile',
      },
      {
        path: 'schedule',
        component: DoctorScheduleComponent,
        title: 'Doctor Schedule'
      },
      {
        path: 'calendar',
        component: DoctorCalendarComponent,
        title: 'Doctor Calendar'
      },
      {
        path: 'appointment',
        component: DoctorAppointmentComponent,
        title: 'Doctor Appointments'
      }
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class DoctorRoutingModule {}
