import { Component, OnInit } from '@angular/core';
import { PatientService } from '../../../../core/services/patient.service';

@Component({
  selector: 'app-patient-sidebar',
  templateUrl: './patient-sidebar.component.html',
  styleUrl: './patient-sidebar.component.css'
})
export class PatientSidebarComponent implements OnInit {
  patientProfile: any = {};

  constructor(private patientService: PatientService) {}

  ngOnInit() {
    this.patientService.getPatientProfile().subscribe(profile => {
      this.patientProfile = profile;
    });
  }

  logout() {
    // Implement logout functionality
  }
}
