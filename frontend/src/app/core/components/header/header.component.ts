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
  @Input() isLoggedIn: boolean = false; // Receive isLoggedIn from login component
  patientProfile: any; // Variable to store patient profile data

  constructor(
    public accessChecker: NbAccessChecker,
    private patientService: PatientService,
    private authService: AuthService // Inject AuthService
  ) {}

  ngOnInit(): void {
      this.fetchPatientProfile();
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
