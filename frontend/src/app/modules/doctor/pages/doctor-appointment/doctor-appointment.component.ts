import { Component, OnInit } from '@angular/core';
import { AppointmentService } from '../../../../core/services/appointment.service';
import { Router } from '@angular/router';
import {
  ApiResponse,
  AppointmentResponse,
  RescheduleAppointment,
} from '../../../../core/models/appointment.model';
import { MessageService } from 'primeng/api';
import { TimeSlot } from '../../../../core/models/booking.model';
import { FormGroup } from '@angular/forms';
import { Option } from '../../../../core/models/other.model';
@Component({
  selector: 'app-doctor-appointment',
  templateUrl: './doctor-appointment.component.html',
  styleUrl: './doctor-appointment.component.css',
})
export class DoctorAppointmentComponent implements OnInit {
  appointments: any[] = [];
  filteredAppointments: any[] = [];
  totalAppointments: number = 0;
  pageSize: number = 5;
  currentPage: number = 1;
  visible: boolean = false;
  selectedAppointment: any = null;
  rescheduleVisible: boolean = false;
  timeSlotSelected: TimeSlot;
  formBookingDate: FormGroup;
  isLoading: boolean = false;
  filterOptions: Option[] | undefined;
  selectedOption: Option | undefined;

  constructor(
    private appointmentService: AppointmentService,
    private router: Router,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.fetchGetListAppointmentsOfDoctor(1, this.pageSize);
    this.filterOptions = [
      { name: 'All', code: 'ALL' },
      { name: 'Accept', code: 'ACCEPT' },
      { name: 'Pending', code: 'PENDING' },
      { name: 'Rescheduled', code: 'RESCHEDULED' },
      { name: 'Cancelled', code: 'CANCELLED' },
    ];
    this.selectedOption = { name: 'All', code: 'ALL' };
  }

  applyFilter(event: any) {
    const filter = event.value?.code;
    if (filter === 'ALL' || !filter) {
      this.filteredAppointments = [...this.appointments];
    } else {
      this.filteredAppointments = [...this.appointments].filter(
        (app) => app.appointmentStatus == filter
      );
    }
  }

  handleSelectedSlot(timeslot: TimeSlot) {
    this.timeSlotSelected = timeslot;
  }

  formatDateAppointment(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-GB', {
      day: '2-digit',
      month: 'short',
      year: 'numeric',
    });
  }

  fetchGetListAppointmentsOfDoctor(page: number, pageSize: number): void {
    this.appointmentService
      .getListAppointmentsOfDoctor(page, pageSize)
      .subscribe(
        (response) => {
          if (response.statusCode === 200) {
            this.appointments = response.data;
            this.filteredAppointments = [...this.appointments];
            this.totalAppointments = (response.totalPage - 1) * pageSize + this.filteredAppointments.length;
          }
        },
        (error) => {
          console.error('Error fetching all appointments of doctor', error);
        }
      );
  }

  handlePageEvent(event: any): void {
    this.currentPage = event.page + 1;
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
            this.fetchGetListAppointmentsOfDoctor(
              this.currentPage,
              this.pageSize
            );
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

  onChangeInProgressOfAppointment(id: number) {
    this.appointmentService.changeInProgressOfAppointmentByDoctor(id).subscribe(
      (response: ApiResponse) => {
        if (response.statusCode === 200) {
          this.messageService.add({
            key: 'messageToast',
            severity: 'success',
            summary: 'Success',
            detail: response.message || 'Status changed successfully',
          });
          setTimeout(() => {
            this.fetchGetListAppointmentsOfDoctor(
              this.currentPage,
              this.pageSize
            );
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
            this.fetchGetListAppointmentsOfDoctor(
              this.currentPage,
              this.pageSize
            );
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
    this.timeSlotSelected = null;
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

  formatDate(date: Date): string {
    return new Date(date).toISOString().split('T')[0];
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
            setTimeout(() => {
              this.fetchGetListAppointmentsOfDoctor(
                this.currentPage,
                this.pageSize
              );
              this.closeRescheduleDialog();
            }, 1000);
          } else {
            this.messageService.add({
              key: 'messageToast',
              severity: 'error',
              summary: 'Error',
              detail: res.message,
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

  // Click view Patient Profile
  viewPatientProfile(patientId: string): void {
    this.router.navigate([`/doctor/patient-profile`, patientId]);
  }
}
