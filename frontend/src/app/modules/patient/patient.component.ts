import { Component, OnInit } from '@angular/core';
import { PatientService } from '../../core/services/patient.service';
@Component({
  selector: 'app-patient',
  templateUrl: './patient.component.html',
  styleUrl: './patient.component.css'
})
export class PatientComponent implements OnInit{
  patientProfile: any; // Variable to store patient profile data

  constructor(
    private patientService: PatientService
  ) {}

  ngOnInit(): void {
      this.fetchPatientProfile();
  }

  fetchPatientProfile(): void {
    this.patientService.getPatientProfile().subscribe(
      (response) => {
        if (response !=null) {
          this.patientProfile = response; // Store the data part of the response
          console.log('Patient Profile:', this.patientProfile); // Log the data
        }
      },
      (error) => {
        console.error('Error fetching patient profile:', error);
      }
    );
  }
}
