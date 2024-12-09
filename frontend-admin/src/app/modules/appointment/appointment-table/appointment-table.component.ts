import { Component, OnInit } from '@angular/core';
import {
  Appointment,
  AppointmentResponse,
} from '../../../core/models/appointment';
import { AppoinmentService } from '../../../core/services/appoinment.service';
import { Specialty } from '../../../core/models/speciality';
import { SpecialtyService } from '../../../core/services/specialty.service';
import { DoctorService } from '../../../core/services/doctor.service';
import { Doctor } from '../../../core/models/doctor';
import { PatientService } from '../../../core/services/patient.service';
import { Patient } from '../../../core/models/patient';
import { MedicalRecordResponse } from '../../../core/models/medical-record';
import { MedicalRecordService } from '../../../core/services/medical-record.service';
interface Column {
  field: string;
  header: string;
  customExportHeader?: string;
}

interface ExportColumn {
  title: string;
  dataKey: string;
}
@Component({
  selector: 'app-appointment-table',
  templateUrl: './appointment-table.component.html',
  styleUrl: './appointment-table.component.css',
})
export class AppointmentTableComponent implements OnInit {
  visibleAppointment: any;
  loadingFetchingData: boolean = true;
  appointments: Appointment[];
  doctorFullName: String;
  appointment: Appointment;
  appointmentRespone: AppointmentResponse;
  specialty: Specialty;
  selectedAppointments: Appointment[];
  patientAge: number;
  doctor: Doctor;
  patient: Patient;
  latestAppointmentDate: Date;
  latestReason: string;

  cols!: Column[];

  exportColumns!: ExportColumn[];
  medical_records: MedicalRecordResponse[];

  constructor(
    private appointmentService: AppoinmentService,
    private doctorService: DoctorService,
    private patientService: PatientService,
    private specialtyService: SpecialtyService,
    private medicalRecordService: MedicalRecordService
  ) {}
  ngOnInit(): void {
    this.loadingFetchingData = true;
    this.getListAppoiment();
    this.cols = [
      { field: 'id', header: 'Id', customExportHeader: 'Appointment Id' },
      { field: 'bookingHour', header: 'Booking Hour' },
      { field: 'dateBooking', header: 'Date Booking' },
      { field: 'fullName', header: 'Patient' },
      { field: 'doctor.specialty.specialtyName', header: 'Specialty' },
      { field: 'doctor.fullName', header: 'Doctor' },
      { field: 'appointmentStatus', header: 'Status' },
    ];
    this.exportColumns = this.cols.map((col) => ({
      title: col.header,
      dataKey: col.field,
    }));
  }
  getListAppoiment() {
    this.appointmentService.getListAppointment().subscribe({
      next: (resp) => {
        this.appointments = resp;
        this.loadingFetchingData = false;
        console.log(resp)
      },
    });
  }
  openEditDialog(appointment: Appointment) {
    this.appointment = { ...appointment };
    this.patientService.getPatientDetail(this.appointment.patientId).subscribe({
      next: (resp) => {
        this.patientAge = this.calculateAge(resp.data.dateOfBirth);
        this.patient = resp.data;
      },
    });
    this.appointmentService.getAppointmentDetail(appointment.id).subscribe({
      next: (resp) => {
        this.appointmentRespone = resp.data;
      },
    });
    this.appointmentService
      .getListAppointmentOfAPatient(this.appointment.patientId)
      .subscribe({
        next: (resp) => {
          this.latestAppointmentDate = resp.data[0].dateBooking;
          this.latestReason = resp.data[0].reason;
        },
      });
    this.medicalRecordService
      .getListMedicalRecordByPatient(this.appointment.patientId)
      .subscribe({
        next: (resp) => {
          this.medical_records = resp.data;
          console.log(resp.data);
        },
      });
    this.visibleAppointment = true;
  }

  calculateAge(birthDateString: string): number {
    // Convert the input string to a Date object
    const birthDate = new Date(birthDateString);
    const currentDate = new Date();

    // Calculate the difference in years
    let age = currentDate.getFullYear() - birthDate.getFullYear();

    // Adjust if the current date is before the birthday this year
    const monthDifference = currentDate.getMonth() - birthDate.getMonth();
    if (
      monthDifference < 0 ||
      (monthDifference === 0 && currentDate.getDate() < birthDate.getDate())
    ) {
      age--;
    }

    return age;
  }

  getSeverity(status: string) {
    switch (status) {
      case 'COMPLETED':
        return 'success';
      case 'ACCEPT':
        return 'info';
      case 'PENDING':
        return 'secondary';
      case 'RESCHEDULED':
        return 'warning';
      case 'CANCELLED':
        return 'danger';
      default:
        return null;
    }
  }
}
