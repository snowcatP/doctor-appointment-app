import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-booking-appointment-index',
  templateUrl: './booking-appointment-index.component.html',
  styleUrl: './booking-appointment-index.component.css',
})
export class BookingAppointmentIndexComponent implements OnInit {
  formBooking: FormGroup;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.createForm();
  }

  createForm() {
    this.formBooking = this.fb.group({
      specialty: [''],
      doctor: [''],
      fullName: [''],
      phone: [''],
      reason: [''],
    });
  }
}
