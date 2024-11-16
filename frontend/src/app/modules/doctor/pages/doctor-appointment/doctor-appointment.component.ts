import { Component } from '@angular/core';
import { AppointmentService } from '../../../../core/services/appointment.service';
import { Router } from '@angular/router';
import { ApiResponse, RescheduleAppointment } from '../../../../core/models/appointment.model';
import { MessageService } from 'primeng/api';
import { TimeSlot } from '../../../../core/models/booking.model';
import { FormBuilder, FormGroup } from '@angular/forms';
import { AppointmentsBooked, AppointmentSlot } from '../../../../core/models/booking.model';
@Component({
  selector: 'app-doctor-appointment',
  templateUrl: './doctor-appointment.component.html',
  styleUrl: './doctor-appointment.component.css'
})
export class DoctorAppointmentComponent {
  appointments: any[] = [];
  totalAppointments: number = 0;
  pageSize: number = 12;
  currentPage: number = 1;
  visible: boolean = false;
  selectedAppointment: any = null;
  rescheduleVisible: boolean = false;
  dateToday: Date = new Date();
  schedules: any[] = [];
  timeSlotSelected: TimeSlot;
  formBookingDate: FormGroup;
  isLoading: boolean = false;
  appointmentsBooked: AppointmentsBooked[] = [];
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
    private appointmentService: AppointmentService,
    private router: Router,
    private messageService: MessageService,
  ) { }

  ngOnInit(): void {
    this.fetchGetListAppointmentsOfDoctor(1, this.pageSize);
    this.generateAppointmentSlots();
  }

  formatDateAppointment(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-GB', {
      day: '2-digit',
      month: 'short',
      year: 'numeric'
    });
  }

  fetchGetListAppointmentsOfDoctor(page: number, pageSize: number): void {
    this.appointmentService.getListAppointmentsOfDoctor(page, pageSize).subscribe(
      (response) => {
        if (response.statusCode === 200) {
          this.appointments = response.data.map((appointment: { dateBooking: string; }) => {
            appointment.dateBooking = this.formatDateAppointment(appointment.dateBooking);
            return appointment;
          });
          this.totalAppointments = response.totalPage * pageSize;
        }
      },
      (error) => {
        console.error('Error fetching all appointments of doctor', error);
      }
    );
  }

  handlePageEvent(event: any): void {
    this.currentPage = (event.page + 1);
    this.pageSize = event.rows;
    this.fetchGetListAppointmentsOfDoctor(this.currentPage, this.pageSize);
  }

  onPageSizeChange(): void {
    this.currentPage = 1;
    this.fetchGetListAppointmentsOfDoctor(this.currentPage, this.pageSize);
  }

  viewAppointmentDetails(appointment: any): void {
    this.selectedAppointment = appointment;
    this.visible = true;
  }

  onChangeStatusAppointment(id: number) {
    this.appointmentService.changeStatusAppointmentByDoctor(id).subscribe(
      (response: ApiResponse) => {
        if (response.statusCode === 200) {
          this.messageService.add({
            key: 'messageToast',
            severity: 'success',
            summary: 'Success',
            detail: response.message || 'Status changed successfully',
          });
          setTimeout(() => {
            this.fetchGetListAppointmentsOfDoctor(this.currentPage, this.pageSize);
          }, 1000);
        } else {
          this.messageService.add({
            key: 'messageToast',
            severity: 'error',
            summary: 'Error',
            detail: response.message || 'Failed to change status',
          });
        }
      },
      (error) => {
        console.error('Failed to change status', error);
        this.messageService.add({
          key: 'messageToast',
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to change status due to server error',
        });
      }
    );
  }

  onCancelAppointment(id: number) {
    this.appointmentService.cancelAppointmentByDoctor(id).subscribe(
      (response: ApiResponse) => {
        if (response.statusCode === 200) {
          this.messageService.add({
            key: 'messageToast',
            severity: 'success',
            summary: 'Success',
            detail: response.message || 'Status changed successfully',
          });

          setTimeout(() => {
            this.fetchGetListAppointmentsOfDoctor(this.currentPage, this.pageSize);
          }, 1000);
        } else {
          this.messageService.add({
            key: 'messageToast',
            severity: 'error',
            summary: 'Error',
            detail: response.message || 'Failed to change status',
          });
        }
      },
      (error) => {
        console.error('Failed to change status', error);
        this.messageService.add({
          key: 'messageToast',
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to change status due to server error',
        });
      }
    );
  }

  // Open reschedule dialog and set selected appointment
  openRescheduleDialog(appointment: any): void {
    this.selectedAppointment = appointment;
    this.rescheduleVisible = true;
  }

  closeRescheduleDialog(): void {
    this.rescheduleVisible = false;
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
    this.isLoading = true;
    this.appointmentService
      .getAppointmentsForRescheduling()
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

  formatDate(date: Date): string {
    return new Date(date).toISOString().split('T')[0];
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

  submitRescheduleAppointment() {
    const bookingData: RescheduleAppointment = {
      dateBooking: this.timeSlotSelected.date,
      bookingHour: this.timeSlotSelected.time
    };
    this.appointmentService.rescheduleAppointmentByDoctor(this.selectedAppointment.id,bookingData).subscribe({
      next: (res) => {
        if (res.statusCode === 200) {
          this.messageService.add({
            key: 'messageToast',
            severity: 'success',
            summary: 'Success',
            detail: 'Reschedule successfully!'
          });
          setTimeout(() => {
            this.fetchGetListAppointmentsOfDoctor(this.currentPage, this.pageSize);
            this.getAppointmentsBooked();
            this.handleAppointmentsBooked();
            this.generateAppointmentSlots();
            this.closeRescheduleDialog();
          }, 2000);
        } else {
          this.messageService.add({
            key: 'messageToast',
            severity: 'error',
            summary: 'Error',
            detail: res.message
          });
        }
      },
      error: (err) => {
        this.messageService.add({
          key: 'messageToast',
          severity: 'error',
          summary: 'Error',
          detail: 'Reschedule unsuccessfully!'
        });
        console.log(err);
      },
    })
  }
}
