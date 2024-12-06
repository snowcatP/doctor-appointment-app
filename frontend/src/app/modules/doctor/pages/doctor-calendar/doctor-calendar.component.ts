import { Component, OnInit, ViewChild } from '@angular/core';
import { CalendarOptions, EventInput } from '@fullcalendar/core';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import dayGridPlugin from '@fullcalendar/daygrid';
import {
  AppointmentResponse,
  RescheduleAppointment,
} from '../../../../core/models/appointment.model';
import { AppointmentService } from '../../../../core/services/appointment.service';
import { FullCalendarComponent } from '@fullcalendar/angular';
import listPlugin from '@fullcalendar/list';
import { ConfirmPopup } from 'primeng/confirmpopup';
import { ConfirmationService, MessageService } from 'primeng/api';
import { TimeSlot } from '../../../../core/models/booking.model';

@Component({
  selector: 'app-doctor-calendar',
  templateUrl: './doctor-calendar.component.html',
  styleUrl: './doctor-calendar.component.css',
})
export class DoctorCalendarComponent implements OnInit {
  @ViewChild(ConfirmPopup) confirmPopup!: ConfirmPopup;
  @ViewChild('calendar') calendarComponent: FullCalendarComponent;
  calendarOptions: CalendarOptions;
  appointmentEvents: EventInput[] = [];
  appointments: AppointmentResponse[] = [];
  isLoading: boolean = false;
  selectedAppointment: AppointmentResponse = new AppointmentResponse();
  visible: boolean = false;
  visibleSchedule: boolean = false;
  timeSlotSelected: TimeSlot;
  constructor(
    private appointmentService: AppointmentService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.calendarInit();
    this.getData();
  }

  calendarInit() {
    this.isLoading = true;
    this.calendarOptions = {
      initialView: 'dayGridMonth',
      plugins: [dayGridPlugin, interactionPlugin, timeGridPlugin, listPlugin],
      headerToolbar: {
        left: 'timeGridDay,timeGridWeek,dayGridMonth,listWeek',
        center: 'title',
        right: 'today,prev,next',
      },
      titleFormat: { year: 'numeric', month: 'short', day: 'numeric' },
      views: {
        day: {
          type: 'timeGridDay',
          slotMinTime: '09:00:00',
          slotMaxTime: '17:00:00',
        },
        week: {
          type: 'timeGridWeek',
          duration: { days: 7 },
          slotMinTime: '09:00:00',
          slotMaxTime: '17:00:00',
        },
      },
      nowIndicator: true,
      expandRows: true,
      eventClick: (arg) => this.handleEventClick(arg),
      eventMouseEnter: (arg) => this.handleHoverEvent(arg),
      eventMouseLeave: (arg) => this.handleLeaveHoverEvent(arg),
    };
  }

  handleSelectedSlot(timeslot: TimeSlot) {
    this.timeSlotSelected = timeslot;
  }

  submitRescheduleAppointment() {
    const bookingData: RescheduleAppointment = {
      dateBooking: this.timeSlotSelected.date,
      bookingHour: this.timeSlotSelected.time,
    };
    this.appointmentService
      .rescheduleAppointment(this.selectedAppointment.id, bookingData)
      .subscribe({
        next: (res) => {
          if (res.statusCode === 200) {
            this.messageService.add({
              key: 'messageToast',
              severity: 'success',
              summary: 'Success',
              detail: 'Reschedule successfully!',
            });
            this.getData();
            setTimeout(() => {
              this.closeModals();
            }, 1000);
          } else {
            this.messageService.add({
              key: 'messageToast',
              severity: 'error',
              summary: 'Error',
              detail: 'Reschedule unsuccessfully!',
            });
          }
        },
        error: (err) => {
          this.messageService.add({
            key: 'messageToast',
            severity: 'error',
            summary: 'Error',
            detail: 'Reschedule unsuccessfully!',
          });
          console.log(err);
        },
      });
  }

  closeModals() {
    this.visibleSchedule = false;
    this.visible = false;
  }

  handleLeaveHoverEvent(arg: any) {
    this.confirmationService.close();
  }

  handleEventClick(arg: any) {
    this.selectedAppointment = this.appointments.find(
      (app) => app.id == arg.event.id
    );
    this.visible = true;
  }

  handleHoverEvent(arg: any) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      header: arg.event.title,
      message: arg.event.startStr,
      acceptLabel: arg.event.groupId, // replace for displaying appointment status
    });
  }

  getData() {
    this.isLoading = true;
    this.appointmentService.getAllAppointmentsByDoctorEmail().subscribe({
      next: (res) => {
        this.appointments = res;
        setTimeout(() => {
          this.processAppointmentData();
          this.isLoading = false;
        }, 100);
      },
      error: (err) => {
        console.log(err);
        this.isLoading = false;
      },
    });
  }

  processAppointmentData() {
    this.isLoading = true;
    if (this.appointmentEvents.length > 0) this.appointmentEvents = [];
    this.appointments.forEach((appointment) => {
      const date = this.formatDate(appointment.dateBooking);
      const bookingHours = this.formatBookingHour(appointment.bookingHour);
      // if (appointment.appointmentStatus != 'RESCHEDULED') {
        this.appointmentEvents.push({
          id: appointment.id.toString(),
          title: `Meet ${appointment.fullName}`,
          start: `${date}T${bookingHours[0]}`,
          end: `${date}T${bookingHours[1]}`,
          backgroundColor: this.statusColor(appointment.appointmentStatus),
          groupId: `${appointment.appointmentStatus}`,
        } as EventInput);
      // }
    });
    this.calendarOptions.events = this.appointmentEvents;
    this.isLoading = false;
  }

  statusColor(status: string) {
    switch (status) {
      case 'PENDING':
        return 'yellow';
      case 'CANCELLED':
        return 'red';
      case 'COMPLETED':
        return '#09e5ab';
      case 'RESCHEDULED':
        return 'black';
      case 'ACCEPT':
        return '#00E65B';
      default:
        return '#f8f9fa';
    }
  }

  checkValidDate(app: AppointmentResponse) {
    return app.dateBooking ? new Date() < new Date(app.dateBooking) : true;
  }

  formatDate(date: Date): string {
    return new Date(date).toISOString().split('T')[0];
  }

  formatBookingHour(time: string): string[] {
    const result: string[] = [];
    const times = time.split(' - ');
    times.forEach((t) => {
      result.push(`${t}:00`);
    });
    return result;
  }
}
