import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-patient-sidebar',
  templateUrl: './patient-sidebar.component.html',
  styleUrl: './patient-sidebar.component.css'
})
export class PatientSidebarComponent {
  @Input() patientProfile: any; // Input to receive patient profile data
  logout() {
    
  }
}
