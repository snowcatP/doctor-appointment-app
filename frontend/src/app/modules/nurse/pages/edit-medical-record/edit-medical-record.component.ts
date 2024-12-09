import { Component } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AppointmentService } from '../../../../core/services/appointment.service';
import { MessageService } from 'primeng/api';
import { AddMedicalRecordRequest, EditMedicalRecordRequest, ApiResponse } from '../../../../core/models/medical-record.model';
import { MedicalRecordService } from '../../../../core/services/medical-record.service';
@Component({
  selector: 'app-edit-medical-record',
  templateUrl: './edit-medical-record.component.html',
  styleUrl: './edit-medical-record.component.css'
})
export class EditMedicalRecordComponent {
  selectedAppointment: any;
  selectedFile?: File;
  imagePreview: string | ArrayBuffer | null = null;
  fileName: string | null = null; // Biến để lưu tên tệp không phải là ảnh
  editingMedicalRecord: EditMedicalRecordRequest = new EditMedicalRecordRequest();
  loading: boolean = false; // Add loading state
  medicalRecordId: number;
  constructor(private router: Router,
    private route: ActivatedRoute,
    private appointmentService: AppointmentService,
    private messageService: MessageService,
    private medicalRecordService: MedicalRecordService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.medicalRecordId = +params.get('id')!; // Lấy id của bác sĩ từ route parameter
      this.fetchDetailMedicalRecord(this.medicalRecordId);
    });
  }

  fetchDetailMedicalRecord(id:number):void{
    this.medicalRecordService.getMedicalRecordDetailById(id).subscribe(
      (response) => {
        if (response.statusCode === 200) {
          this.editingMedicalRecord = {...response.data};
          this.editingMedicalRecord.medicalRecordId = response.data.id;
        }
      },
      (error) => {
        console.error('Error fetching Medical Record', error);
      }
    );
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

  onUpdate(): void {
    this.loading = true;
    console.log(this.editingMedicalRecord)
    this.medicalRecordService.editMedicalRecordForPatient(this.editingMedicalRecord, this.selectedFile).subscribe(
      (response: ApiResponse) => {
        this.loading = false; // Stop loading
        if (response.statusCode === 200) {
          this.messageService.add({
            key: 'messageToast',
            severity: 'success',
            summary: 'Success',
            detail: response.message || 'Medical Record edited successfully',
          });
          
          setTimeout(() => {
            this.closeForm();
            this.router.navigate(['/nurse/dashboard']);
          }, 1000);
        } else {
          this.messageService.add({
            key: 'messageToast',
            severity: 'error',
            summary: 'Error',
            detail: response.message || 'Failed to edit Medical Record',
          });
        }
      },
      (error) => {
        this.loading = false; // Stop loading
        console.error('Failed to edit Medical Record', error);
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
