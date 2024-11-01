import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { map, Observable, startWith } from 'rxjs';
import { BookingService } from '../../../../../core/services/booking.service';
import {
  BookingData,
  DoctorBooking,
  Specialty,
} from '../../../../../core/models/booking.model';
import { SpecialtyService } from '../../../../../core/services/specialty.service';
import { RxwebValidators } from '@rxweb/reactive-form-validators';

@Component({
  selector: 'app-booking-appointment-index',
  templateUrl: './booking-appointment-index.component.html',
  styleUrl: './booking-appointment-index.component.css',
})
export class BookingAppointmentIndexComponent implements OnInit {
  formBooking: FormGroup;
  formBookingDate: FormGroup;
  filteredSpecialties: Observable<Specialty[]>;
  filteredDoctors: Observable<DoctorBooking[]>;
  listDoctors: DoctorBooking[] = [];
  listSpecialties: Specialty[] = [];
  doctorSelected: DoctorBooking;
  bookingData: BookingData = new BookingData();
  schedules: any[];
  dateToday: Date = new Date();
  constructor(
    private fb: FormBuilder,
    private bookingService: BookingService,
    private specialtyService: SpecialtyService
  ) {}

  ngOnInit(): void {
    this.createForm();
    this.getData();

    this.schedules = [
      [
        {
          dayWeek: 'Sun',
          date: '2/11'
        },
        {
          dayWeek: 'Mon',
          date: '3/11'
        },
        {
          dayWeek: 'Tue',
          date: '4/11'
        },
        {
          dayWeek: 'Wed',
          date: '5/11'
        },
        {
          dayWeek: 'Thu',
          date: '6/11'
        },
        {
          dayWeek: 'Fri',
          date: '7/11'
        },
      ],
      [
        {
          dayWeek: 'Sat',
          date: '8/11'
        },
        {
          dayWeek: 'Sun',
          date: '9/11'
        },
        {
          dayWeek: 'Mon',
          date: '10/11'
        },
        {
          dayWeek: 'Tue',
          date: '11/11'
        },
        {
          dayWeek: 'Wed',
          date: '12/11'
        },
        {
          dayWeek: 'Fri',
          date: '13/11'
        },
      ]
    ]
  }

  getData() {
    this.bookingService.getDoctors().subscribe({
      next: (res) => {
        this.listDoctors = res;
      },
      error: (err) => {
        console.log(err);
      },
    });

    this.specialtyService.getListSpecialty().subscribe({
      next: (res) => {
        if (res.data) {
          res.data.forEach((specialty: Specialty) => {
            this.listSpecialties.push(specialty);
          });
          this.filteredSpecialties = this.formBooking.controls[
            'specialty'
          ].valueChanges.pipe(
            startWith(''),
            map((value) => this._filterSpecialty(value || ''))
          );
        }
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  specialtyChange() {
    this.filteredDoctors = this.formBooking.controls[
      'doctor'
    ].valueChanges.pipe(
      startWith(''),
      map((value) => this._filterDoctor(value || ''))
    );
  }

  selectDoctor() {
    if (this.formBooking.get('doctor').getRawValue() != '') {
      this.doctorSelected = this.listDoctors.find(
        (doctor) =>
          doctor.fullName === this.formBooking.get('doctor').getRawValue()
      );
    }
  }

  checkValidInformationForm() {
    // if (
    //   (this.formBooking.controls['specialty'].invalid || this.formBooking.controls['specialty'].getRawValue() == '') ||
    //   (this.formBooking.controls['doctor'].invalid || this.formBooking.controls['doctor'].getRawValue() == '') ||
    //   (this.formBooking.controls['firstName'].invalid || this.formBooking.controls['firstName'].getRawValue() == '') ||
    //   (this.formBooking.controls['lastName'].invalid || this.formBooking.controls['lastName'].getRawValue() == '') ||
    //   (this.formBooking.controls['email'].invalid || this.formBooking.controls['email'].getRawValue() == '') ||
    //   (this.formBooking.controls['phone'].invalid || this.formBooking.controls['phone'].getRawValue() == '') ||
    //   (this.formBooking.controls['reason'].invalid || this.formBooking.controls['reason'].getRawValue() == '')
    // ) {
    //   return false;
    // }
    return true;
  }

  createForm() {
    this.formBooking = this.fb.group({
      specialty: ['', [RxwebValidators.required()]],
      doctor: ['', [RxwebValidators.required()]],
      firstName: ['', [RxwebValidators.required(), RxwebValidators.alpha()]],
      lastName: ['', [RxwebValidators.required(), RxwebValidators.alpha()]],
      email: ['', [RxwebValidators.required(), RxwebValidators.email()]],
      phone: ['', [RxwebValidators.required(), RxwebValidators.numeric()]],
      reason: ['', [RxwebValidators.required()]],
    });

    this.formBookingDate = this.fb.group({
      bookingDate: ['', [RxwebValidators.required()]],
      bookingHour: ['', [RxwebValidators.required()]],
    })
  }

  private _filterSpecialty(value: string): Specialty[] {
    const filterValue = value.toLowerCase();
    return this.listSpecialties.filter((specialty) =>
      specialty.specialtyName.toLowerCase().includes(filterValue)
    );
  }

  private _filterDoctor(value: string): DoctorBooking[] {
    const filterValue = value.toLowerCase();
    return this.listDoctors.filter((doctor) =>
      doctor.fullName.toLowerCase().includes(filterValue)
    );
  }
}
