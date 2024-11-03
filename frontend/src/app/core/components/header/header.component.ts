import { Component, OnInit, Input } from '@angular/core';
import { NbAccessChecker } from '@nebular/security';
import { MenuItem } from 'primeng/api';
import { PatientService } from '../../services/patient.service';
import { AuthService } from '../../services/auth.service';
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
})
export class HeaderComponent implements OnInit {
  isLoggedIn: boolean = false;
  patientProfile: any; // Variable to store patient profile data

  constructor(
    private authService: AuthService,
    private patientService: PatientService
  ) {}

  ngOnInit(): void {
    this.authService.currentLoginStatus.subscribe((status) => {
      this.isLoggedIn = status;
    });
    if (this.isLoggedIn) {
      this.fetchPatientProfile();
    }
  }

  fetchPatientProfile(): void {
    this.patientService.getPatientProfile().subscribe(
      (response) => {
        if (response.statusCode === 200) {
          this.patientProfile = response; // Store the data part of the response
          console.log(this.patientProfile);
        }
      },
      (error) => {
        console.error('Error fetching patient profile:', error);
      }
    );
  }
}
