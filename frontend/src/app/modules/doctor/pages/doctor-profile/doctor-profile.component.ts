import { Component } from '@angular/core';
import { UpdateDoctorProfileRequest, ApiResponse } from '../../../../core/models/doctor.model';
import { DoctorService } from '../../../../core/services/doctor.service';
import { MessageService } from 'primeng/api';
import { Router } from '@angular/router';
import { error } from 'jquery';
@Component({
  selector: 'app-doctor-profile',
  templateUrl: './doctor-profile.component.html',
  styleUrl: './doctor-profile.component.css'
})
export class DoctorProfileComponent {
  originalDoctorProfile: any = {}; // Store the original profile
  updateProfile: UpdateDoctorProfileRequest = new UpdateDoctorProfileRequest();
  selectedFile?: File;
  imagePreview: string | ArrayBuffer | null = null;
  loading: boolean = false; // Add loading state

  constructor(
    private doctorService: DoctorService,
    private messageService: MessageService,
    private route: Router,
  ) { }
  
  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
    if (this.selectedFile) {
      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreview = reader.result;
      };
      reader.readAsDataURL(this.selectedFile);
    }
  }
  ngOnInit() {

    this.doctorService.getDoctorProfile().subscribe(profile => {
      profile.data.dateOfBirth = new Date(profile.data.dateOfBirth);
      this.originalDoctorProfile = profile.data;
      this.updateProfile = { ...profile.data };
    });
  }


  onSubmit() {
    this.loading = true; // Start loading
    this.doctorService.updateDoctorProfile(this.updateProfile, this.selectedFile).subscribe(
      (response: ApiResponse) => {
        this.loading = false; // Stop loading
        if (response.statusCode === 200) {
          this.messageService.add({
            key: 'messageToast',
            severity: 'success',
            summary: 'Success',
            detail: response.message || 'Profile updated successfully',
          });

         // Clear the selected file and image preview after successful update
         this.selectedFile = null;
         this.imagePreview = null;

          setTimeout(() => {
            this.doctorService.getDoctorProfile().subscribe(profile => {
              this.doctorService.setProfile(profile.data);
          });
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
        this.loading = false; // Stop loading
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
