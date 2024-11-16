import { Component, OnInit,TemplateRef, ViewChild } from '@angular/core';
import { PatientService } from '../../../../core/services/patient.service';
import { Router } from '@angular/router';
import { MedicalRecordService } from '../../../../core/services/medical-record.service';
@Component({
  selector: 'app-patient-dashboard',
  templateUrl: './patient-dashboard.component.html',
  styleUrl: './patient-dashboard.component.css'
})
export class PatientDashboardComponent {
  appointments: any[] = [];
  totalAppointments: number = 0; 
  pageSize: number = 10; 
  currentPage: number = 1;

  visible: boolean = false; // Control dialog visibility
  selectedAppointment: any = null; // Store selected appointment for dialog

  constructor(private patientService: PatientService, 
    private router: Router,
    private medicalRecordService: MedicalRecordService) {}

  ngOnInit(): void {
    this.fetchAppointmentsOfPatient(1, this.pageSize);
    this.fetchMedicalRecordsOfPatient(1,this.pageSizeMR);
  }

  onAppointmentsFound(appointments: any[]): void {
    this.appointments = appointments;
    this.totalAppointments = appointments.length; // Cập nhật tổng số bác sĩ
  }
  
  fetchAppointmentsOfPatient(page: number, pageSize: number): void {
    this.patientService.getAppointmentsOfPatient(page, pageSize).subscribe(
      (response) => {
        if (response.statusCode === 200) {
          this.appointments = response.data;
          this.totalAppointments = response.totalPage * pageSize;
        }
      },
      (error) => {
        console.error('Error fetching appointments', error);
      }
    );
  }
  

  handlePageEvent(event: any): void {
    this.currentPage = (event.page + 1);
    this.pageSize = event.rows;
    this.fetchAppointmentsOfPatient(this.currentPage, this.pageSize);
  }

  onPageSizeChange(): void {
    this.currentPage = 1;
    this.fetchAppointmentsOfPatient(this.currentPage, this.pageSize);
  }

  viewDoctorProfile(doctorId: number): void {
    this.router.navigate(['/doctor-profile/', doctorId]); // Điều hướng với id của bác sĩ
  }

  // Open dialog and set selected appointment
  viewAppointmentDetails(appointment: any): void {
    this.selectedAppointment = appointment;
    this.visible = true;
  }

  medicalRecords: any[] = [];
  totalMedicalRecords: number = 0; 
  pageSizeMR: number = 10; 
  currentPageMR: number = 1; 

  fetchMedicalRecordsOfPatient(page: number, pageSize: number): void {
    this.medicalRecordService.getMedicalRecordsOfPatient(page, pageSize).subscribe(
      (response) => {
        if (response.statusCode === 200) {
          this.medicalRecords = response.data;
          console.log(this.medicalRecords)
          this.totalMedicalRecords = response.totalPage * pageSize;
        }
      },
      (error) => {
        console.error('Error fetching appointments', error);
      }
    );
  }

  visibleMedicalRecord: boolean = false; 
  selectedMedicalRecord: any = null; 

  viewMedicalRecordDetails(medicalRecord: any): void {
    this.selectedMedicalRecord = medicalRecord;
    this.visibleMedicalRecord = true;
  }
}
