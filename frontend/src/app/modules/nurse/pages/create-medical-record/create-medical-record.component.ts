import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AppointmentService } from '../../../../core/services/appointment.service';
import { MessageService } from 'primeng/api';
import { AddMedicalRecordRequest, ApiResponse } from '../../../../core/models/medical-record.model';
import { MedicalRecordService } from '../../../../core/services/medical-record.service';
@Component({
  selector: 'app-create-medical-record',
  templateUrl: './create-medical-record.component.html',
  styleUrl: './create-medical-record.component.css'
})
export class CreateMedicalRecordComponent{
  selectedAppointment: any;
  selectedFile?: File;
  imagePreview: string | ArrayBuffer | null = null;
  fileName: string | null = null; // Biến để lưu tên tệp không phải là ảnh
  medicalRecordRequest: AddMedicalRecordRequest = new AddMedicalRecordRequest();
  loading: boolean = false; // Add loading state

  constructor(private router: Router,
    private appointmentService: AppointmentService,
    private messageService: MessageService,
    private medicalRecordService: MedicalRecordService
  ) {}

  ngOnInit(): void {
    this.selectedAppointment = this.appointmentService.getAppointment();
  }

  

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
    if (this.selectedFile) {
      if (this.selectedFile.type.startsWith('image/')) {
        const reader = new FileReader();
        reader.onload = () => {
          this.imagePreview = reader.result;
          this.fileName = null; // Đặt lại fileName khi tệp là ảnh
        };
        reader.readAsDataURL(this.selectedFile);
      } else {
        // Nếu là tệp không phải ảnh, hiển thị tên tệp
        this.imagePreview = null; // Đặt lại imagePreview khi tệp không phải ảnh
        this.fileName = this.selectedFile.name;
      }
    }
  }

  closeForm() {
    this.router.navigate(['/nurse/dashboard']);
  }

  onSubmit() {
    this.loading = true; // Start loading
    this.medicalRecordRequest.patientId = this.selectedAppointment?.patientResponse?.id; //Set id of patient
    this.medicalRecordRequest.appointmentId = this.selectedAppointment.id;
    console.log(this.medicalRecordRequest)
    this.medicalRecordService.addMedicalRecordForPatient(this.medicalRecordRequest, this.selectedFile).subscribe(
      (response: ApiResponse) => {
        this.loading = false; // Stop loading
        if (response.statusCode === 200) {
          this.messageService.add({
            key: 'messageToast',
            severity: 'success',
            summary: 'Success',
            detail: response.message || 'Medical Record added successfully',
          });

          // Clear the selected file and image preview after successful update
          this.selectedFile = null;
          this.imagePreview = null;

          setTimeout(() => {
            this.closeForm();
            this.router.navigate(['/nurse/dashboard']);
          }, 1000);
        } else {
          this.messageService.add({
            key: 'messageToast',
            severity: 'error',
            summary: 'Error',
            detail: response.message || 'Failed to add Medical Record',
          });
        }
      },
      (error) => {
        this.loading = false; // Stop loading
        console.error('Failed to add Medical Record', error);
        this.messageService.add({
          key: 'messageToast',
          severity: 'error',
          summary: 'Error',
          detail: 'Failed due to server error',
        });
      }
    );
  }
}
