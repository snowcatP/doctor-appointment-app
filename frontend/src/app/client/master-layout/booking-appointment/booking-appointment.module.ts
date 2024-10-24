import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { BookingAppointmentRoutingModule } from './booking-appointment-routing.module';
import { BookingAppointmentCheckoutComponent } from './booking-appointment-checkout/booking-appointment-checkout.component';
import { BookingAppointmentSuccessComponent } from './booking-appointment-success/booking-appointment-success.component';
import { BookingAppointmentIndexComponent } from './booking-appointment-index/booking-appointment-index.component';
import { BookingAppointmentComponent } from './booking-appointment.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatStepperModule } from '@angular/material/stepper';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { ImageModule } from 'primeng/image';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [
    BookingAppointmentComponent,
    BookingAppointmentCheckoutComponent,
    BookingAppointmentSuccessComponent,
    BookingAppointmentIndexComponent,
  ],
  imports: [
    CommonModule,
    BookingAppointmentRoutingModule,
    MatFormFieldModule,
    MatInputModule,
    MatAutocompleteModule,
    FormsModule,
    ReactiveFormsModule,
    MatStepperModule,
    MatIconModule,
    MatButtonModule,
    ImageModule,
    RouterModule
  ],
})
export class BookingAppointmentModule {}
