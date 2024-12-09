import { LoginRequest } from './../../../../../core/models/authentication.model';
import {
  Component,
  ElementRef,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import {
  filter,
  map,
  Observable,
  startWith,
  Subject,
  Subscription,
  takeUntil,
} from 'rxjs';
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
import { Router, ActivatedRoute  } from '@angular/router';
import { Store } from '@ngrx/store';
import * as fromAuth from '../../../../../core/states/auth/auth.reducer';
import * as AuthActions from '../../../../../core/states/auth/auth.actions';
import { User } from '../../../../../core/models/authentication.model';
import { WebSocketService } from '../../../../../core/services/webSocket.service';
import { Actions, ofType } from '@ngrx/effects';
import * as CryptoJS from 'crypto-js';
@Component({
  selector: 'app-booking-appointment-index',
  templateUrl: './booking-appointment-index.component.html',
  styleUrl: './booking-appointment-index.component.css',
})
export class BookingAppointmentIndexComponent implements OnInit, OnDestroy {
  @ViewChild('emailInput') emailInput: ElementRef;
  isLoading: boolean = false;
  loading: boolean = false;
  formBooking: FormGroup;
  formBookingDate: FormGroup;
  formLogin: FormGroup;
  filteredSpecialties: Observable<Specialty[]>;
  filteredDoctors: Observable<DoctorBooking[]>;
  listDoctors: DoctorBooking[] = [];
  listSpecialties: Specialty[] = [];
  doctorSelected: DoctorBooking;
  doctorSelected$: Observable<DoctorBooking>;
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
  isLogged$: Observable<boolean>;
  isLogged: boolean;
  user$: Observable<User>;
  user: User;
  modalVisible: boolean = false;
  showPass: boolean = false;
  loginErrorMessage: string = '';
  doctorId: number;
  private bookingSubscription: Subscription;
  private unsubscribe$ = new Subject<void>();
  constructor(
    private fb: FormBuilder,
    private router: Router,
    private doctorService: DoctorService,
    private specialtyService: SpecialtyService,
    private appointmentService: AppointmentService,
    private messageService: MessageService,
    private store: Store<fromAuth.State>,
    private webSocketService: WebSocketService,
    private actions$: Actions, // effects
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.createForm();
    this.generateAppointmentSlots();
    this.getData();
    this.getObservables();
    // this.webSocketInit();
    this.subscribeToActions();

    const secretKey = '28a57933ee4343d000fe4d347ac74dc96ea35c699c1de470b68c7741b26a513f';

    this.route.queryParams.subscribe(params => {
      if (params['doctorId']) {
        const decryptedId = CryptoJS.AES.decrypt(params['doctorId'], secretKey).toString(CryptoJS.enc.Utf8);
        this.doctorId = parseInt(decryptedId, 10);
        if (this.doctorId) {
          this.doctorSelected = this.doctorSelected || new DoctorBooking();
          this.doctorSelected.id = this.doctorId;
  
          this.getDoctorDetails(this.doctorSelected.id);
        }
      }
    });

  }

  getDoctorDetails(id: number): void {
    this.doctorService.getDoctorDetail(id).subscribe(
      (response) => {
        if (response.statusCode === 200) {
          this.doctorSelected = response.data;
          // Update the form with doctor details
          this.formBooking.patchValue({
            doctor: this.doctorSelected.fullName,
            specialty: this.doctorSelected.specialty.specialtyName
        });
        }
      },
      (error) => {
        console.error('Error fetching doctor details', error);
      }
    );
  }

  viewDoctorProfile(doctorId: number): void {
    this.router.navigate(['/doctor-profile/', doctorId]); // Điều hướng với id của bác sĩ
  }

  ngOnDestroy(): void {}

  subscribeToActions() {
    this.actions$
      .pipe(ofType(AuthActions.loginSuccess), takeUntil(this.unsubscribe$))
      .subscribe(() => {
        this.messageService.add({
          key: 'messageToast',
          severity: 'success',
          summary: 'Success',
          detail: 'Login successfully',
        });
        this.setUserInformation();
        setTimeout(() => {
          this.closeDialog();
        }, 500);
      });

    this.store.select(fromAuth.selectErrorMessage).subscribe((error) => {
      if (error) {
        this.loginErrorMessage = 'Wrong email or password.';
        setTimeout(() => {
          this.emailInput.nativeElement.select();
        }, 0);
      }
    });
  }

  login() {
    const credential: LoginRequest = {
      email: this.formLogin.controls['email'].value,
      password: this.formLogin.controls['password'].value,
    };
    this.store.dispatch(AuthActions.loginRequest({ credential }));
  }

  getObservables() {
    this.isLogged$ = this.store.select(fromAuth.selectIsLogged);
    this.isLogged$.subscribe((res) => (this.isLogged = res as boolean));
    this.user$ = this.store.select(fromAuth.selectUser);
    this.user$.subscribe((res) => (this.user = res as User));
    this.setUserInformation();
  }

  setUserInformation() {
    if (this.isLogged) {
      this.formBooking.patchValue({
        firstName: this.user.firstName,
        lastName: this.user.lastName,
        email: this.user.email,
        phone: this.user.phone,
      });
    }
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
            this.isLoading = false;
            setTimeout(() => {
              this.handleAppointmentsBooked();
            }, 100);
          },
          error: (err) => {
            console.log(err);
            this.isLoading = false;
          },
        });
    }
  }

  submitAppointment() {
    if (this.isLogged) {
      this.loading = true; // Start loading
      const bookingData: BookingDataPatient = {
        doctorId: this.doctorSelected.id,
        patientId: this.user.id,
        dateBooking: this.timeSlotSelected.date,
        bookingHour: this.timeSlotSelected.time,
        doctorName: this.doctorSelected.fullName,
        reason: this.formBooking.get('reason').value,
      };
      this.appointmentService.createAppointmentByPatient(bookingData).subscribe({
        next: (res) => {
          console.log(res)
          if (res.statusCode === 200) {
            this.setAppointmentForSuccess(null, res?.data);
            this.messageService.add({
              key: 'messageToast',
              severity: 'success',
              summary: 'Success',
              detail: 'Booked appointment successfully!'
            });
            setTimeout(() => {
              this.loading = false; // Stop loading
              this.router.navigate([
                '/booking/success',
                { bookingSuccess: true },
              ]);
            }, 2000);
          } else {
            this.messageService.add({
              key: 'messageToast',
              severity: 'error',
              summary: 'Error',
              detail: 'Booked appointment unsuccessfully!'
            });
            this.loading = false; // Stop loading
          }
        },
        error: (err) => {
          this.messageService.add({
            key: 'messageToast',
            severity: 'error',
            summary: 'Error',
            detail: 'Booked appointment unsuccessfully!'
          });
          this.loading = false; // Stop loading
          console.log(err);
        },
      })
    } else {
      this.loading = true; // Start loading
      const bookingData: BookingDataGuest = {
        doctorId: this.doctorSelected.id,
        doctorName: this.doctorSelected.fullName,
        fullName:
          this.formBooking.get('firstName').value + ' ' +
          this.formBooking.get('lastName').value,
        phone: this.formBooking.get('phone').value,
        email: this.formBooking.get('email').value,
        reason: this.formBooking.get('reason').value,
        dateBooking: this.timeSlotSelected.date,
        bookingHour: this.timeSlotSelected.time,
      };
      this.appointmentService
        .createAppointmentByGuest(bookingData)
        .subscribe({
          next: (res) => {
            if (res.statusCode === 200) {
              this.setAppointmentForSuccess(res?.data, null);
              this.messageService.add({
                key: 'messageToast',
                severity: 'success',
                summary: 'Success',
                detail: 'Booked appointment successfully!'
              });
              setTimeout(() => {
                this.loading = false; // Stop loading
                this.router.navigate([
                  '/booking/success',
                  { bookingSuccess: true },
                ]);
              }, 2000);
            }else {
              this.messageService.add({
                key: 'messageToast',
                severity: 'error',
                summary: 'Error',
                detail: 'Booked appointment unsuccessfully!'
              });
              this.loading = false; // Stop loading
            }
          },
          error: (err) => {
            this.messageService.add({
              key: 'messageToast',
              severity: 'error',
              summary: 'Error',
              detail: 'Booked appointment unsuccessfully!'
            });
            this.loading = false; // Stop loading
            console.log(err);
          },
        });
    }
  }

  setAppointmentForSuccess(
    guest: BookingDataGuest,
    patient: BookingDataPatient
  ) {
    if (guest != null) {
      this.appointmentService.setAppointmentBookedGuest(guest);
    }
    if (patient != null) {
      this.appointmentService.setAppointmentBookedPatient(patient);
    }

  }

  closeBooking() {
    this.router.navigate(['/']);
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
        this.filteredDoctors = this.formBooking.controls[
          'doctor'
        ].valueChanges.pipe(
          startWith(''),
          map((value) => this._filterDoctor(value || ''))
        );
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
      if (this.doctorSelected) {
        this.formBooking
          .get('specialty')
          .setValue(this.doctorSelected.specialty.specialtyName);
      }
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
      phone: [
        '',
        [
          RxwebValidators.required(),
          RxwebValidators.digit(),
          RxwebValidators.minLength({ value: 8 }),
          RxwebValidators.maxLength({ value: 15 }),
        ],
      ],
      reason: ['', [RxwebValidators.required()]],
    });

    this.formBookingDate = this.fb.group({
      bookingDate: ['', [RxwebValidators.required()]],
      bookingHour: ['', [RxwebValidators.required()]],
    });

    this.formLogin = this.fb.group({
      email: ['', [RxwebValidators.email(), RxwebValidators.required()]],
      password: ['', [RxwebValidators.required()]],
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
      && doctor.specialty.specialtyName === this.formBooking.controls['specialty'].value
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
    this.showToast(true, 'yes');
  }

  showToast(status: boolean, message: string) {
    this.messageService.add({
      severity: status ? 'success' : 'error',
      summary: status ? 'Success' : 'Error',
      detail: message,
      life: 1500  // Thời gian hiển thị toast (ms)
    });
  }

  showDialog() {
    this.modalVisible = true;
  }

  closeDialog() {
    this.modalVisible = false;
    this.formLogin.reset();
  }

  navigateToRegister() {
    const url = this.router.serializeUrl(
      this.router.createUrlTree(['/auth/register'])
    );
    window.open(url, '_blank');
  }

  showPassword() {
    this.showPass = !this.showPass;
  }
}
