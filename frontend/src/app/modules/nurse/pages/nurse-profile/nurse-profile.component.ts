import { Component } from '@angular/core';
import { UpdateNurseProfileRequest, ApiResponse } from '../../../../core/models/nurse.model';
import { NurseService } from '../../../../core/services/nurse.service';
import { MessageService } from 'primeng/api';
import { Router } from '@angular/router';
@Component({
  selector: 'app-nurse-profile',
  templateUrl: './nurse-profile.component.html',
  styleUrl: './nurse-profile.component.css'
})
export class NurseProfileComponent {
  originalNurseProfile: any = {}; // Store the original profile
  updateProfile: UpdateNurseProfileRequest = new UpdateNurseProfileRequest();
  selectedFile?: File;
  imagePreview: string | ArrayBuffer | null = null;
  loading: boolean = false; // Add loading state

  constructor(
    private nurseService: NurseService,
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

    this.nurseService.getNurseProfile().subscribe(profile => {
      profile.data.dateOfBirth = new Date(profile.data.dateOfBirth);
      this.originalNurseProfile = profile.data;
      this.updateProfile = { ...profile.data };
    });
  }


  onSubmit() {
    this.loading = true; // Start loading
    this.nurseService.updateNurseProfile(this.updateProfile, this.selectedFile).subscribe(
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
            this.nurseService.getNurseProfile().subscribe(profile => {
              this.nurseService.setProfile(profile.data);
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
