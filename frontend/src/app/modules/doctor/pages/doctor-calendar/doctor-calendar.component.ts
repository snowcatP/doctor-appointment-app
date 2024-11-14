import { Component, OnInit, ViewChild } from '@angular/core';
import { CalendarOptions, EventInput } from '@fullcalendar/core';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import dayGridPlugin from '@fullcalendar/daygrid';
import { AppointmentResponse } from '../../../../core/models/appointment.model';
import { AppointmentService } from '../../../../core/services/appointment.service';
import { FullCalendarComponent } from '@fullcalendar/angular';
import listPlugin from '@fullcalendar/list';
import { ConfirmPopup } from 'primeng/confirmpopup';
import { ConfirmationService } from 'primeng/api';

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
  constructor(
    private appointmentService: AppointmentService,
    private confirmationService: ConfirmationService
  ) {}

  ngOnInit(): void {
    this.calendarInit();
    this.getData();
  }

  calendarInit() {
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
      expandRows: true,
      eventClick: (arg) => this.handleEventClick(arg),
      eventMouseEnter: (arg) => this.handleHoverEvent(arg),
      eventMouseLeave: (arg) => this.handleLeaveHoverEvent(arg),
    };
  }

  accept() {}

  handleLeaveHoverEvent(arg: any) {
    this.confirmationService.close();
  }

  handleEventClick(arg: any) {
    this.selectedAppointment = this.appointments.find(app => app.id == arg.event.id);
    if (this.selectedAppointment != null) {
      this.visible = true;
    }
  }

  handleHoverEvent(arg: any) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: arg.event.title,
  });
  }

  getData() {
    this.isLoading = true;
    this.appointmentService.getAllAppointmentsByDoctorEmail().subscribe({
      next: (res) => {
        this.appointments = res;
        this.isLoading = false;
        setTimeout(() => {
          this.processAppointmentData();
        }, 100);
      },
      error: (err) => {
        console.log(err);
        this.isLoading = false;
      },
    });
  }

  processAppointmentData() {
    this.appointments.forEach((appointment) => {
      const date = this.formatDate(appointment.dateBooking);
      const bookingHours = this.formatBookingHour(appointment.bookingHour);
      this.appointmentEvents.push({
        id: appointment.id.toString(),
        title: `Meet ${appointment.fullName}`,
        start: `${date}T${bookingHours[0]}`,
        end: `${date}T${bookingHours[1]}`,
      } as EventInput);
    });
    this.calendarOptions.events = this.appointmentEvents;
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
