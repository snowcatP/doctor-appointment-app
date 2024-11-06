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
  patientProfile: any;

  constructor(private authService: AuthService,private patientService: PatientService) {}

  ngOnInit(): void {
    this.authService.currentLoginStatus.subscribe((status) => {
      this.isLoggedIn = status;
    });

    // Lấy thông tin bệnh nhân từ API và thiết lập
    this.patientService.fetchMyInfo().subscribe(profile => {
      this.patientService.setPatientProfile(profile);
    });

    // Đăng ký để nhận patientProfile
    this.patientService.getPatientProfile().subscribe(profile => {
      this.patientProfile = profile;
    });
  }
}
