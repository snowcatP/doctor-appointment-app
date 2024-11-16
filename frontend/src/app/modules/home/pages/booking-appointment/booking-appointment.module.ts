import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { BookingAppointmentRoutingModule } from './booking-appointment-routing.module';
import { BookingAppointmentIndexComponent } from './booking-appointment-index/booking-appointment-index.component';
import { BookingAppointmentSuccessComponent } from './booking-appointment-success/booking-appointment-success.component';
import { MatIconModule } from '@angular/material/icon';
import { MatStepperModule } from '@angular/material/stepper';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { ImageModule } from 'primeng/image';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { BookingAppointmentComponent } from './booking-appointment.component';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { DividerModule } from 'primeng/divider';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatTabsModule } from '@angular/material/tabs';
import { CarouselModule } from 'primeng/carousel';
import { CalendarModule } from 'primeng/calendar';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { ToastModule } from 'primeng/toast';
import { DialogModule } from 'primeng/dialog';
import { SharedModule } from "../../../../shared/shared.module";

@NgModule({
  declarations: [
    BookingAppointmentIndexComponent,
    BookingAppointmentSuccessComponent,
    BookingAppointmentComponent,
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
    RouterModule,
    DividerModule,
    MatTooltipModule,
    MatTabsModule,
    CarouselModule,
    CalendarModule,
    ProgressSpinnerModule,
    ToastModule,
    DialogModule,
    SharedModule
],
})
export class BookingAppointmentModule {}
