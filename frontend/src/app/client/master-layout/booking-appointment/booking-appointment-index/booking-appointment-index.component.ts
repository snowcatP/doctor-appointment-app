import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { map, Observable, startWith } from 'rxjs';

@Component({
  selector: 'app-booking-appointment-index',
  templateUrl: './booking-appointment-index.component.html',
  styleUrl: './booking-appointment-index.component.css',
})
export class BookingAppointmentIndexComponent implements OnInit {
  formBooking: FormGroup;
  options: string[] = ['One', 'Two', 'Three'];
  filteredOptions: Observable<string[]>;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.createForm();

    this.filteredOptions = this.formBooking.controls['specialty'].valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value || '')),
    );
  }

  createForm() {
    this.formBooking = this.fb.group({
      specialty: [''],
      doctor: [''],
      firstName: [''],
      lastName: [''],
      email: [''],
      phone: [''],
      reason: [''],
    });
  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();

    return this.options.filter(option => option.toLowerCase().includes(filterValue));
  }
}
