import { Component, OnInit } from '@angular/core';
import { PatientService } from '../../../../core/services/patient.service';
import { AuthService } from '../../../../core/services/auth.service';
@Component({
  selector: 'app-patient-sidebar',
  templateUrl: './patient-sidebar.component.html',
  styleUrl: './patient-sidebar.component.css'
})
export class PatientSidebarComponent implements OnInit {
  patientProfile: any = {};

  constructor(private authService: AuthService) {}

  ngOnInit() {
    this.authService.getUserData().subscribe(profile => {
        this.patientProfile = profile;
      
    });

    this.authService.userData$.subscribe(profile => {
      if (profile) {
        this.patientProfile = profile;
      }
    });
  }

  logout() {
    // Implement logout functionality
  }
}
