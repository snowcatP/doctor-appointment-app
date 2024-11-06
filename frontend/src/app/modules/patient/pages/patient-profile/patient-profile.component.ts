import { Component, OnInit } from '@angular/core';
import { PatientService } from '../../../../core/services/patient.service';
import { UpdateProfileRequest } from '../../../../core/models/patient.model';
import { MessageService } from 'primeng/api';
import { ApiResponse } from '../../../../core/models/patient.model';
import { AuthService } from '../../../../core/services/auth.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-patient-profile',
  templateUrl: './patient-profile.component.html',
  styleUrl: './patient-profile.component.css'
})
export class PatientProfileComponent implements OnInit {
  originalProfile: any = {}; // Store the original profile
  updateProfile: UpdateProfileRequest = new UpdateProfileRequest();

  constructor(
    private patientService: PatientService,
    private messageService: MessageService,
    private authService: AuthService,
    private route: Router,
  ) {}

  ngOnInit() {

    this.authService.getUserData().subscribe(profile => {
      this.originalProfile = { ...profile }; // Store original profile
      profile.dateOfBirth = new Date(profile.dateOfBirth);
      this.updateProfile = { ...profile }; // Create a copy for editing
    });
  }

  get genderLabel(): string {
    return this.updateProfile?.gender ? 'Male' : 'Female';
  }

  onSubmit() {
    this.patientService.updatePatientProfile(this.updateProfile).subscribe(
      (response: ApiResponse) => {
        if (response.statusCode === 200) {
          this.messageService.add({
            key: 'messageToast',
            severity: 'success',
            summary: 'Success',
            detail: response.message || 'Profile updated successfully',
          });
          //Send update user data event
          this.authService.setUserData(this.updateProfile);
          setTimeout(() => {
                this.route.navigateByUrl('/patient/dashboard');
              }, 1000);
        } else {
          this.messageService.add({
            key: 'messageToast',
            severity: 'error',
            summary: 'Error',
            detail: response.message || 'Failed to update profile',
          });
        }
      },
      (error) => {
        console.error('Failed to update profile', error);
        this.messageService.add({
          key: 'messageToast',
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to update profile due to server error',
        });
      }
    );
  }
}
