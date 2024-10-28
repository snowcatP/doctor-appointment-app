import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BookingAppointmentComponent } from './booking-appointment.component';
import { BookingAppointmentIndexComponent } from './booking-appointment-index/booking-appointment-index.component';
import { BookingAppointmentSuccessComponent } from './booking-appointment-success/booking-appointment-success.component';

const routes: Routes = [
  {
    path: '',
    component: BookingAppointmentComponent,
    children: [
      {
        path: '',
        redirectTo: '/booking/appointment',
        pathMatch: 'full',
      },
      {
        path: 'appointment',
        component: BookingAppointmentIndexComponent,
        title: 'Booking an Appointment',
      },
      {
        path: 'success',
        component: BookingAppointmentSuccessComponent,
        title: 'Booking an Appointment | Success',
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BookingAppointmentRoutingModule { }
