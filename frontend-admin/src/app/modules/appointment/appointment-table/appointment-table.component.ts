import { Component, OnInit } from '@angular/core';
import { Appointment, AppointmentResponse } from '../../../core/models/appointment';
import { AppoinmentService } from '../../../core/services/appoinment.service';
import { Specialty } from '../../../core/models/speciality';
import { SpecialtyService } from '../../../core/services/specialty.service';
import { DoctorService } from '../../../core/services/doctor.service';
import { Doctor } from '../../../core/models/doctor';
import { PatientService } from '../../../core/services/patient.service';
import { Patient } from '../../../core/models/patient';

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

  constructor(
    private appointmentService: AppoinmentService,
    private doctorService: DoctorService,
    private patientService: PatientService,
    private specialtyService: SpecialtyService
  ) {}
  ngOnInit(): void {
    this.loadingFetchingData = true;
    this.getListAppoiment();
  }
  getListAppoiment() {
    this.appointmentService.getListAppointment().subscribe({
      next: (resp) => {
        this.appointments = resp;
        console.log(resp);
        this.loadingFetchingData = false;
      },
    });
  }
  openEditDialog(appointment: Appointment) {
    this.appointment = { ...appointment };
    this.patientAge = this.calculateAge(appointment.patientBirthday);
    this.appointmentService.getAppointmentDetail(appointment.id).subscribe({
      next: (resp)=>{
        this.appointmentRespone = resp.data;
        console.log(resp.data)
      }
    
    })
    // this.doctorService.getDoctorDetail(appointment.doctor.id).subscribe({
    //   next: (resp) => {
    //     this.doctor = resp
    //   },
    // });
    // this.specialtyService.getSpecialtyDetail(this.doctor.specialtyId).subscribe({
    //   next: (resp) => {
    //     this.specialty = resp.data
    //   },
    // })

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
