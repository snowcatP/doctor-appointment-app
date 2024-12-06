import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnDestroy,
  OnInit,
  Output,
  SimpleChanges,
} from '@angular/core';
import {
  AppointmentsBooked,
  AppointmentSlot,
  DoctorBooking,
  TimeSlot,
} from '../../../core/models/booking.model';
import { filter, Observable, Subject, Subscription } from 'rxjs';
import { User } from '../../../core/models/authentication.model';
import { FormBuilder, FormGroup } from '@angular/forms';
import { AppointmentService } from '../../../core/services/appointment.service';
import { BookingNotification } from '../../../core/models/notification.model';
import { WebSocketService } from '../../../core/services/webSocket.service';
import {
  AppointmentResponse,
  GetAppointmentForReschedulingRequest,
} from '../../../core/models/appointment.model';
import * as fromAuth from '../../../core/states/auth/auth.reducer';
import { Store } from '@ngrx/store';
import { RxwebValidators } from '@rxweb/reactive-form-validators';

@Component({
  selector: 'app-schedule',
  templateUrl: './schedule.component.html',
  styleUrl: './schedule.component.css',
})
export class ScheduleComponent implements OnInit, OnChanges, OnDestroy {
  @Input() isReschedule: boolean;
  @Input() doctorSelected: DoctorBooking;
  @Input() selectedApp: AppointmentResponse;
  @Output() selectedSlot = new EventEmitter<TimeSlot>();
  formLogin: FormGroup;
  isLogged$: Observable<boolean>;
  isLogged: boolean;
  user$: Observable<User>;
  user: User;
  isLoading: boolean = false;
  schedules: any[] = [];
  timeSlotSelected: TimeSlot;
  dateToday: Date = new Date();
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
  modalVisible: boolean = false;
  appointmentsBooked: AppointmentsBooked[] = [];
  private bookingSubscription: Subscription;
  private unsubscribe$ = new Subject<void>();
  constructor(
    private appointmentService: AppointmentService,
    private webSocketService: WebSocketService,
    private store: Store<fromAuth.State>
  ) {}

  ngOnInit(): void {
    this.generateAppointmentSlots();
    this.webSocketInit();
  }

  ngOnChanges(changes: SimpleChanges): void {
    // Handle patient and guest booking
    this.isLoading = true;
    this.store.select(fromAuth.selectRole).subscribe((r) => {
      if (this.isReschedule) {
        if (r === 'PATIENT') {
          if (changes.selectedApp?.currentValue != undefined) {
            const request: GetAppointmentForReschedulingRequest = {
              doctorEmail: changes.selectedApp?.currentValue?.doctor?.email,
            };
            this.appointmentService
              .getAppointmentsForReschedulingByPatient(request)
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
        } else {
          this.appointmentService.getAppointmentsForRescheduling().subscribe({
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
      } else {
        if (changes.doctorSelected?.currentValue != undefined) {
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
    });

    // if (changes.selectedApp?.currentValue != undefined) {
    //   this.handleAppointmentsBooked();
    // }
    this.isLoading = false;
  }

  ngOnDestroy(): void {
    // if (this.bookingSubscription) {
    //   this.bookingSubscription.unsubscribe();
    // }
    // this.webSocketService.disconnectSocket();
    // this.unsubscribe$.next();
    // this.unsubscribe$.complete(); // Cleanup subscription on component destroy
  }

  webSocketInit() {
    // this.webSocketService
    //   .connectSocket()
    //   .pipe(filter((state) => state))
    //   .subscribe(() => {
    //     this.bookingSubscription = this.webSocketService
    //       .on('/topic/booking/notifications')
    //       .subscribe((notification: BookingNotification) => {
    //         this.handleAppointmentSendFromWs(notification);
    //       });
    //   });

    // this.bookingSubscription = this.webSocketService
    //   .on('/topic/booking/notifications')
    //   .subscribe((notification: BookingNotification) => {
    //     this.handleAppointmentSendFromWs(notification);
    //     console.log('aa')
    //   });
  }

  handleAppointmentSendFromWs(app: BookingNotification) {
    if (!this.doctorSelected || app.doctor.id !== this.doctorSelected.id) {
      return;
    }
    const bookingDate = this.formatDate(app.dateBooking);
    // if (
    //   this.formatDate(this.timeSlotSelected.date) == bookingDate &&
    //   app.bookingHour == this.timeSlotSelected.time
    // ) {
    //   this.timeSlotSelected = null;
    // }
    this.schedules.forEach((week: AppointmentSlot[]) => {
      week.forEach((day: AppointmentSlot) => {
        day.timeSlotsMorning.forEach((timeSlot: TimeSlot) => {
          const tsDate = this.formatDate(timeSlot.date);
          if (tsDate == bookingDate && timeSlot.time == app.bookingHour) {
            timeSlot.isBooked = true;
            return;
          }
        });
        day.timeSlotsAfternoon.forEach((timeSlot: TimeSlot) => {
          const tsDate = this.formatDate(timeSlot.date);
          if (tsDate == bookingDate && timeSlot.time == app.bookingHour) {
            timeSlot.isBooked = true;
            return;
          }
        });
      });
    });
  }

  handleAppointmentsBooked() {
    this.schedules.forEach((week: AppointmentSlot[]) => {
      week.forEach((day: AppointmentSlot) => {
        day.timeSlotsMorning.forEach(
          (timeSlot: TimeSlot) => {
            timeSlot.isBooked = false;
            timeSlot.isPassedIn = false;
          }
        );
        day.timeSlotsAfternoon.forEach(
          (timeSlot: TimeSlot) => {
            timeSlot.isBooked = false;
            timeSlot.isPassedIn = false;
          }
        );
      });
    });
    this.schedules.forEach((week: AppointmentSlot[]) => {
      week.forEach((day: AppointmentSlot) => {
        this.appointmentsBooked.forEach((appBooked) => {
          day.timeSlotsMorning.forEach((timeSlot: TimeSlot) => {
            const tsDate = this.formatDate(timeSlot.date);
            const appDate = this.formatDate(appBooked.dateBooking);
            if (tsDate == appDate && timeSlot.time == appBooked.bookingHour) {
              timeSlot.isBooked = true;
            }
            if (
              this.selectedApp &&
              this.selectedApp.dateBooking != null &&
              tsDate == this.formatDate(this.selectedApp.dateBooking) &&
              timeSlot.time == this.selectedApp.bookingHour
            ) {
              timeSlot.isPassedIn = true;
            } else {
              timeSlot.isPassedIn = false;
            }
          });
          day.timeSlotsAfternoon.forEach((timeSlot: TimeSlot) => {
            const tsDate = this.formatDate(timeSlot.date);
            const appDate = this.formatDate(appBooked.dateBooking);
            if (tsDate == appDate && timeSlot.time == appBooked.bookingHour) {
              timeSlot.isBooked = true;
            }
            if (
              this.selectedApp &&
              this.selectedApp.dateBooking != null &&
              tsDate == this.formatDate(this.selectedApp.dateBooking) &&
              timeSlot.time == this.selectedApp.bookingHour
            ) {
              timeSlot.isPassedIn = true;
            } else {
              timeSlot.isPassedIn = false;
            }
          });
        });
      });
    });
    this.isLoading = false;
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

  selectBookingDate(slot: TimeSlot) {
    if (slot == this.timeSlotSelected) {
      this.timeSlotSelected = null;
      this.selectedSlot.emit(null);
    } else {
      this.timeSlotSelected = slot;
      this.selectedSlot.emit(this.timeSlotSelected);
    }
  }

  showDialog() {
    this.modalVisible = true;
  }

  closeDialog() {
    this.modalVisible = false;
    this.timeSlotSelected = null;
    this.formLogin.reset();
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

  formatDate(date: Date): string {
    return new Date(date).toISOString().split('T')[0];
  }
}
