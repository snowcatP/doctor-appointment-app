import { Component, OnInit } from '@angular/core';
import { PatientService } from '../../../core/services/patient.service';
import { ActivatedRoute } from '@angular/router';
import { Patient } from '../../../core/models/patient';
import { MedicalRecordService } from '../../../core/services/medical-record.service';
import { MedicalRecordResponse } from '../../../core/models/medical-record';

@Component({
  selector: 'app-patient-dashboard',
  templateUrl: './patient-dashboard.component.html',
  styleUrl: './patient-dashboard.component.css',
})
export class PatientDashboardComponent implements OnInit {
  patient: Patient;
  patientAge: number
  firstMedicalRecord: MedicalRecordResponse;
  medical_records: MedicalRecordResponse[];
  queryParams: any = {};
  constructor(
    private patientService: PatientService,
    private medicalRecordService: MedicalRecordService,
    private route: ActivatedRoute,
  ) {}
  ngOnInit(): void {
    if (this.route.snapshot.queryParamMap.get('id') !== null) {
      this.route.queryParams.subscribe((params) => {
        this.getPatientDetail(params['id']);
        this.getMedicalRecordByPatient(params['id']);
      });
    } else {
      this.getTheFirstPatinet();
      
    }
    
  }
  getMedicalRecordByPatient(id: number){
    this.medicalRecordService
      .getListMedicalRecordByPatient(id)
      .subscribe({
        next: (resp) => {
          console.log(resp.data);
          this.medical_records = resp.data;
          this.firstMedicalRecord = resp.data[0]
        },
      });

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
  getPatientDetail(id: number) {
    this.patientService.getPatientDetail(id).subscribe({
      next: (resp) => {
        console.log(resp);
        this.patient = resp.data;
        this.patientAge = this.calculateAge(resp.data.dateOfBirth)
      },
    });
  }
  getTheFirstPatinet() {
    this.patientService.getListPatient().subscribe({
      next: (resp) => {
        this.getPatientDetail(resp[0].id);
        this.getMedicalRecordByPatient(resp[0].id)
      },
    });
  }

}
