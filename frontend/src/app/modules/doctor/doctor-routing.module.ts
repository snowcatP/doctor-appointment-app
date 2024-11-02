import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DoctorComponent } from './doctor.component';
import { DoctorDashboardComponent } from './pages/doctor-dashboard/doctor-dashboard.component';
import { DoctorProfileComponent } from './pages/doctor-profile/doctor-profile.component';
import { DoctorScheduleComponent } from './pages/doctor-schedule/doctor-schedule.component';
import { DoctorCalendarComponent } from './pages/doctor-calendar/doctor-calendar.component';
import { DoctorAppointmentComponent } from './pages/doctor-appointment/doctor-appointment.component';

const routes: Routes = [
  {
    // doctor
    path: '',
    component: DoctorComponent,
    children: [
      {
        path: '',
        redirectTo: '/doctor/dashboard',
        pathMatch: 'full'
      },
      {
        path: 'dashboard',
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
  exports: [RouterModule]
})
export class DoctorRoutingModule { }