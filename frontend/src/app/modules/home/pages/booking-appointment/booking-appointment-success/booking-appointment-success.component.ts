import { Component, OnInit } from '@angular/core';
import { AppointmentService } from '../../../../../core/services/appointment.service';
import {
  BookingDataGuest,
  BookingDataPatient,
} from '../../../../../core/models/booking.model';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-booking-appointment-success',
  templateUrl: './booking-appointment-success.component.html',
  styleUrl: './booking-appointment-success.component.css',
})
export class BookingAppointmentSuccessComponent implements OnInit {
  appointmentBookedGuestData: any;
  appointmentBookedPatientData: any;
  isFromBooking: boolean = false;
  isGuest: boolean = false;
  isNavigateFromBooking: boolean = false;
  constructor(
    private appointmentService: AppointmentService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const success = this.route.snapshot.paramMap.get('bookingSuccess');
    if (success != 'true') {
      if (!this.isFromBooking) {
        this.router.navigate(['/error404']);
      }
    }

    this.appointmentService.getAppointmentBookedGuest.subscribe((res) => {
      if (res.email != undefined) {
        this.appointmentBookedGuestData = res;
        this.isGuest = true;
      }
    });

    this.appointmentService.getAppointmentBookedPatient.subscribe((res) => {
      if (res.doctor.id != undefined) {
        this.appointmentBookedPatientData = res;
      }
    });
  }
}
