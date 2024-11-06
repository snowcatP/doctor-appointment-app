import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { map, Observable, startWith } from 'rxjs';
import {
  AppointmentsBooked,
  AppointmentSlot,
  BookingDataGuest,
  BookingDataPatient,
  DoctorBooking,
  Specialty,
  TimeSlot,
} from '../../../../../core/models/booking.model';
import { RxwebValidators } from '@rxweb/reactive-form-validators';
import { SpecialtyService } from '../../../../../core/services/specialty.service';
import { AppointmentService } from '../../../../../core/services/appointment.service';
import { DoctorService } from '../../../../../core/services/doctor.service';
import { MessageService } from 'primeng/api';
import { Router } from '@angular/router';
import { AuthService } from '../../../../../core/services/auth.service';

@Component({
  selector: 'app-booking-appointment-index',
  templateUrl: './booking-appointment-index.component.html',
  styleUrl: './booking-appointment-index.component.css',
})
export class BookingAppointmentIndexComponent implements OnInit {
  isLoading: boolean = false;
  formBooking: FormGroup;
  formBookingDate: FormGroup;
  filteredSpecialties: Observable<Specialty[]>;
  filteredDoctors: Observable<DoctorBooking[]>;
  listDoctors: DoctorBooking[] = [];
  listSpecialties: Specialty[] = [];
  doctorSelected: DoctorBooking;
  bookingDataGuest: BookingDataGuest = new BookingDataGuest();
  dateToday: Date = new Date();
  schedules: any[] = [];
  daySlots: any[] = new Array(6);
  appointmentsBooked: AppointmentsBooked[] = [];
  timeSlotSelected: TimeSlot;
  timeSchedulesMorning: any[] = [
    { time: '09:00 - 09:30' },
    { time: '10:00 - 10:30' },
    { time: '11:00 - 11:30' },
  ];
  timeSchedulesAfternoon: any[] = [
    { time: '14:00 - 14:30' },
    { time: '15:00 - 15:30' },
    { time: '16:00 - 16:30' },
  ];

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private doctorService: DoctorService,
    private specialtyService: SpecialtyService,
    private appointmentService: AppointmentService,
    private messageService: MessageService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.createForm();
    this.generateAppointmentSlots();
    this.getData();
  }

  selectBookingDate(slot: TimeSlot) {
    this.timeSlotSelected = slot;
    this.formBookingDate.controls['bookingDate'].setValue(
      this.timeSlotSelected.date
    );
    this.formBookingDate.controls['bookingHour'].setValue(
      this.timeSlotSelected.time
    );
  }

  getAppointmentsBooked() {
    if (this.doctorSelected) {
      this.isLoading = true;
      this.appointmentService
        .getAppointmentsForBooking(this.doctorSelected.id)
        .subscribe({
          next: (res) => {
            this.appointmentsBooked = res;
            this.handleAppointmentsBooked();
            this.isLoading = false;
          },
          error: (err) => {
            console.log(err);
            this.isLoading = false;
          },
        });
    }
  }

  submitAppointment() {
    const bookingAppointmentData: BookingDataGuest = {
      doctorId: this.doctorSelected.id,
      doctorName: this.doctorSelected.fullName,
      fullName:
        this.formBooking.get('firstName').value +
        this.formBooking.get('lastName').value,
      phone: this.formBooking.get('phone').value,
      email: this.formBooking.get('email').value,
      reason: this.formBooking.get('reason').value,
      dateBooking: this.timeSlotSelected.date,
      bookingHour: this.timeSlotSelected.time,
    };
    this.appointmentService
      .createAppointmentByGuest(bookingAppointmentData)
      .subscribe({
        next: (res) => {
          if (res.statusCode == 200) {
            this.setAppointmentForSuccess(res?.data);
            this.showToast(true, 'Booked appointment successfully!');
            setTimeout(() => {
              this.router.navigate([
                '/booking/success',
                { bookingSuccess: true },
              ]);
            }, 2000);
          }
        },
        error: (err) => {
          this.showToast(false, 'Booked appointment unsuccessfully!');
          console.log(err);
        },
      });
  }

  setAppointmentForSuccess(
    guest?: BookingDataGuest,
    patient?: BookingDataPatient
  ) {
    if (guest) {
      this.appointmentService.setAppointmentBookedGuest(guest);
    }
    if (patient) {
      this.appointmentService.setAppointmentBookedPatient(patient);
    }
  }

  handleAppointmentsBooked() {
    this.schedules.forEach((week: AppointmentSlot[]) => {
      week.forEach((day: AppointmentSlot) => {
        this.appointmentsBooked.forEach((appBooked) => {
          day.timeSlotsMorning.forEach((timeSlot: TimeSlot) => {
            const tsDate = this.formatDate(timeSlot.date);
            const appDate = this.formatDate(appBooked.dateBooking);
            if (tsDate == appDate && timeSlot.time == appBooked.bookingHour) {
              timeSlot.isBooked = true;
            }
          });
          day.timeSlotsAfternoon.forEach((timeSlot: TimeSlot) => {
            const tsDate = this.formatDate(timeSlot.date);
            const appDate = this.formatDate(appBooked.dateBooking);
            if (tsDate == appDate && timeSlot.time == appBooked.bookingHour) {
              timeSlot.isBooked = true;
            }
          });
        });
      });
    });
  }

  generateAppointmentSlots() {
    this.schedules = [this.generateSchedule(0), this.generateSchedule(7)];
  }

  generateSchedule(dateStart: number): AppointmentSlot[] {
    const schedule: AppointmentSlot[] = [];
    let currentDate: Date = new Date(
      Date.now() + dateStart * 24 * 60 * 60 * 1000
    );

    let dayLoop = dateStart;
    while (schedule.length < 6) {
      const dayWeek = this.mapDayWeek(
        currentDate.toLocaleDateString('vi-VN', {
          weekday: 'short',
        })
      );

      // Skip Sunday
      if (dayWeek !== 'Sun') {
        const date = `${currentDate.getDate()}/${currentDate.getMonth() + 1}`;
        const slotsMorning: TimeSlot[] = [];
        const slotsAfternoon: TimeSlot[] = [];

        // gen timeslots for morning
        this.timeSchedulesMorning.forEach((time) => {
          slotsMorning.push(new TimeSlot(time.time, currentDate));
        });

        // gen timeslots for afternoon
        this.timeSchedulesAfternoon.forEach((time) => {
          slotsAfternoon.push(new TimeSlot(time.time, currentDate));
        });
        schedule.push(
          new AppointmentSlot(dayWeek, date, slotsMorning, slotsAfternoon)
        );
      }
      dayLoop++;
      currentDate = new Date(Date.now() + dayLoop * 24 * 60 * 60 * 1000);
    }
    return schedule;
  }

  getData() {
    this.doctorService.getDoctorsForBooking().subscribe({
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
    this.getAppointmentsBooked();
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

  createForm() {
    this.formBooking = this.fb.group({
      specialty: ['', [RxwebValidators.required()]],
      doctor: ['', [RxwebValidators.required()]],
      firstName: [
        '',
        [
          RxwebValidators.required(),
          RxwebValidators.pattern({
            expression: {
              name: /^[\w'\-,.][^0-9_!¡?÷?¿/\\+=@#$%ˆ&*(){}|~<>;:[\]]{1,}$/,
            },
          }),
        ],
      ],
      lastName: [
        '',
        [
          RxwebValidators.required(),
          RxwebValidators.pattern({
            expression: {
              name: /^[\w'\-,.][^0-9_!¡?÷?¿/\\+=@#$%ˆ&*(){}|~<>;:[\]]{1,}$/,
            },
          }),
        ],
      ],
      email: ['', [RxwebValidators.required(), RxwebValidators.email()]],
      phone: ['', [RxwebValidators.required(), RxwebValidators.digit()]],
      reason: ['', [RxwebValidators.required()]],
    });

    this.formBookingDate = this.fb.group({
      bookingDate: ['', [RxwebValidators.required()]],
      bookingHour: ['', [RxwebValidators.required()]],
    });
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

  formatDate(date: Date): string {
    return new Date(date).toISOString().split('T')[0];
  }

  mapDayWeek(dayWeek: string) {
    const dayMap = new Map<string, string>([
      ['CN', 'Sun'],
      ['Th 2', 'Mon'],
      ['Th 3', 'Tue'],
      ['Th 4', 'Wed'],
      ['Th 5', 'Thu'],
      ['Th 6', 'Fri'],
      ['Th 7', 'Sat'],
    ]);
    return dayMap.get(dayWeek) || dayWeek;
  }

  testToast() {
    this.showToast(true, "yes");
  }

  showToast(status: boolean, message: string) {
    if (status) {
      this.messageService.add({
        severity: 'success',
        summary: 'Success',
        detail: message
      });
    } else {
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: message
      });
    }
  }
}
