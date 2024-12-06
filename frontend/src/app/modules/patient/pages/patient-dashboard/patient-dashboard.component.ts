import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { PatientService } from '../../../../core/services/patient.service';
import { Router } from '@angular/router';
import { MedicalRecordService } from '../../../../core/services/medical-record.service';
import { ApiResponse } from '../../../../core/models/doctor.model';
import { AppointmentService } from '../../../../core/services/appointment.service';
import { MessageService } from 'primeng/api';
import {
  AppointmentResponse,
  RescheduleAppointment,
} from '../../../../core/models/appointment.model';
import { TimeSlot } from '../../../../core/models/booking.model';
import { Option } from '../../../../core/models/other.model';
@Component({
  selector: 'app-patient-dashboard',
  templateUrl: './patient-dashboard.component.html',
  styleUrl: './patient-dashboard.component.css',
})
export class PatientDashboardComponent {
  appointments: any[] = [];
  filteredAppointments: any[] = [];
  totalAppointments: number = 0;
  pageSize: number = 10;
  currentPage: number = 1;
  visible: boolean = false; // Control dialog visibility
  selectedAppointment: any = null; // Store selected appointment for dialog
  medicalRecords: any[] = [];
  totalMedicalRecords: number = 0;
  pageSizeMR: number = 10;
  currentPageMR: number = 1;
  rescheduleVisible: boolean = false;
  visibleMedicalRecord: boolean = false;
  selectedMedicalRecord: any = null;
  timeSlotSelected: TimeSlot;
  filterOptions: Option[] | undefined;
  selectedOption: Option | undefined;

  constructor(
    private patientService: PatientService,
    private medicalRecordService: MedicalRecordService,
    private appointmentService: AppointmentService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.fetchAppointmentsOfPatient(1, this.pageSize);
    this.fetchMedicalRecordsOfPatient(1, this.pageSizeMR);
    this.filterOptions = [
      { name: 'All', code: 'ALL' },
      { name: 'Accept', code: 'ACCEPT' },
      { name: 'Pending', code: 'PENDING' },
      { name: 'Cancelled', code: 'CANCELLED' },
    ];
    this.selectedOption = { name: 'All', code: 'ALL' };
  }

  onAppointmentsFound(appointments: any[]): void {
    this.appointments = appointments;
    this.totalAppointments = appointments.length; // Cập nhật tổng số bác sĩ
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
              this.fetchAppointmentsOfPatient(this.currentPage, this.pageSize);
              this.closeRescheduleDialog();
            }, 500);
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

  handleSelectedSlot(timeslot: any) {
    this.timeSlotSelected = timeslot;
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
            this.fetchAppointmentsOfPatient(this.currentPage, this.pageSize);
          }, 500);
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

  fetchAppointmentsOfPatient(page: number, pageSize: number): void {
    this.patientService.getAppointmentsOfPatient(page, pageSize).subscribe(
      (response) => {
        if (response.statusCode === 200) {
          this.appointments = response.data;
          this.filteredAppointments = [...this.appointments];
          this.totalAppointments = (response.totalPage - 1) * pageSize + this.filteredAppointments.length;
        }
      },
      (error) => {
        console.error('Error fetching appointments', error);
      }
    );
  }

  handlePageEvent(event: any): void {
    this.currentPage = event.page + 1;
    this.pageSize = event.rows;
    this.fetchAppointmentsOfPatient(this.currentPage, this.pageSize);
  }

  onPageSizeChange(): void {
    this.currentPage = 1;
    this.fetchAppointmentsOfPatient(this.currentPage, this.pageSize);
  }

  viewDoctorProfile(doctorId: number): void {
    window.open(`/doctor-profile/${doctorId}`, '_blank');
  }

  viewAppointmentDetails(appointment: AppointmentResponse): void {
    this.selectedAppointment = appointment;
    this.visible = true;
  }

  openRescheduleDialog(appointment: AppointmentResponse): void {
    this.selectedAppointment = appointment;
    this.rescheduleVisible = true;
  }

  fetchMedicalRecordsOfPatient(page: number, pageSize: number): void {
    this.medicalRecordService
      .getMedicalRecordsOfPatient(page, pageSize)
      .subscribe(
        (response) => {
          if (response.statusCode === 200) {
            this.medicalRecords = response.data;
            this.totalMedicalRecords = response.totalPage * pageSize;
          }
        },
        (error) => {
          console.error('Error fetching appointments', error);
        }
      );
  }

  viewMedicalRecordDetails(medicalRecord: any): void {
    this.selectedMedicalRecord = medicalRecord;
    this.visibleMedicalRecord = true;
  }

  closeRescheduleDialog() {
    this.rescheduleVisible = false;
    this.selectedAppointment = null;
  }
}
