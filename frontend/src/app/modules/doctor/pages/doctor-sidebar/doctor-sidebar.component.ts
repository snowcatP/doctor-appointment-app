import { Component } from '@angular/core';
import { DoctorService } from '../../../../core/services/doctor.service';
@Component({
  selector: 'app-doctor-sidebar',
  templateUrl: './doctor-sidebar.component.html',
  styleUrl: './doctor-sidebar.component.css'
})
export class DoctorSidebarComponent {
  doctorProfile: any = {};

  constructor(private doctorService: DoctorService) {}

  ngOnInit() {
    // Subscribe to the profile observable to get live updates
    this.doctorService.userData$.subscribe(profile => {
      if (profile) {
        this.doctorProfile = profile;
      }
    });

    this.doctorService.getDoctorProfile().subscribe(profile => {
        this.doctorProfile = profile.data;
    });
  }
}
