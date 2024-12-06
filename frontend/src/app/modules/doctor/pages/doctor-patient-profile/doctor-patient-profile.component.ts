import { Component } from '@angular/core';
import { PatientService } from '../../../../core/services/patient.service';
import { ActivatedRoute } from '@angular/router';
import { AppointmentService } from '../../../../core/services/appointment.service';
import { MedicalRecordService } from '../../../../core/services/medical-record.service';
import { MatDialog } from '@angular/material/dialog';
import { AddMedicalRecordRequest, ApiResponse, EditMedicalRecordRequest } from '../../../../core/models/medical-record.model';
import { MessageService } from 'primeng/api';
import { MatTabChangeEvent } from '@angular/material/tabs';
@Component({
  selector: 'app-doctor-patient-profile',
  templateUrl: './doctor-patient-profile.component.html',
  styleUrl: './doctor-patient-profile.component.css'
})
export class DoctorPatientProfileComponent {
  patientId!: number;
  patientDetail: any = {};

  appointments: any[] = [];
  totalAppointments: number = 0;
  pageSize: number = 10;
  currentPage: number = 1;
  

  constructor(
    private patientService: PatientService,
    private route: ActivatedRoute,
    private appointmentService: AppointmentService,
    private medicalRecordService: MedicalRecordService,
    private messageService: MessageService,
    public dialog: MatDialog) { }

  ngOnInit() {
    // Get id from URL
    this.route.paramMap.subscribe(params => {
      this.patientId = +params.get('id')!;
      this.fetchPatientDetails(this.patientId);
      this.fetchAppointmentsOfPatientByPatientID(this.patientId, 1, this.pageSize);
    });
  }

  fetchPatientDetails(id: number): void {
    this.patientService.getPatientDetail(id).subscribe(
      (response) => {
        if (response.statusCode === 200) {
          this.patientDetail = response.data;
        }
      },
      (error) => {
        console.error('Error fetching patient details', error);
      }
    );
  }

  calculateAge(dateOfBirth: string): number {
    const birthDate = new Date(dateOfBirth);
    const ageDiffMs = Date.now() - birthDate.getTime();
    const ageDate = new Date(ageDiffMs);
    return Math.abs(ageDate.getUTCFullYear() - 1970);
  }

  fetchAppointmentsOfPatientByPatientID(id: number, page: number, pageSize: number): void {
    this.appointmentService.getAppointmentsOfPatientByPatientId(id, page, pageSize).subscribe(
      (response) => {
        if (response.statusCode === 200) {
          this.appointments = response.data;
          this.totalAppointments = response.totalPage * pageSize;
        }
      },
      (error) => {
        console.error('Error fetching appointments', error);
      }
    );
  }

  handlePageEvent(event: any): void {
    this.currentPage = (event.page + 1);
    this.pageSize = event.rows;
    this.fetchAppointmentsOfPatientByPatientID(this.patientId, this.currentPage, this.pageSize);
  }

  onPageSizeChange(): void {
    this.currentPage = 1;
    this.fetchAppointmentsOfPatientByPatientID(this.patientId, this.currentPage, this.pageSize);
  }

  medicalRecords: any[] = [];
  totalMedicalRecords: number = 0;
  pageSizeMR: number = 10;
  currentPageMR: number = 1;

  fetchMedicalRecordsOfPatientByPatientID(id: number, page: number, pageSize: number): void {
    this.medicalRecordService.getMedicalRecordsOfPatientByPatientId(id, page, pageSize).subscribe(
      (response) => {
        if (response.statusCode === 200) {
          this.medicalRecords = response.data;
          this.totalMedicalRecords = response.totalPage * pageSize;
        }
      },
      (error) => {
        console.error('Error fetching Medical Record', error);
      }
    );
  }

  onTabChange(event: MatTabChangeEvent): void {
    if (event.index === 0) {
      this.fetchAppointmentsOfPatientByPatientID(this.patientId, 1, this.pageSize);
    } else if (event.index === 1) {
      this.fetchMedicalRecords();
    }
  }
  

  visible: boolean = false; // Control dialog visibility
  selectedAppointment: any = null; // Store selected appointment for dialog

  visibleMedicalRecord: boolean = false;
  selectedMedicalRecord: any = null;

  // Open dialog and set selected appointment
  viewAppointmentDetails(appointment: any): void {
    this.selectedAppointment = appointment;
    this.visible = true;
  }

  viewMedicalRecordDetails(medicalRecord: any): void {
    this.selectedMedicalRecord = medicalRecord;
    this.visibleMedicalRecord = true;
  }

  isFormVisible = false;

  openForm(appointment:any) {
    this.isFormVisible = true;
    this.selectedAppointment = appointment;
  }

  closeForm() {
    this.isFormVisible = false;
  }



  selectedFile?: File;
  imagePreview: string | ArrayBuffer | null = null;
  fileName: string | null = null; // Biến để lưu tên tệp không phải là ảnh
  medicalRecordRequest: AddMedicalRecordRequest = new AddMedicalRecordRequest();
  loading: boolean = false; // Add loading state

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

  onSubmit() {
    this.loading = true; // Start loading
    this.medicalRecordRequest.patientId = this.patientId; //Set id of patient
    this.medicalRecordRequest.appointmentId = this.selectedAppointment.id;
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
            this.fetchMedicalRecordsOfPatientByPatientID(this.patientId, 1, this.pageSizeMR)
            this.fetchAppointmentsOfPatientByPatientID(this.patientId, 1, this.pageSize);
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

  isEditFormVisible = false;
  editingMedicalRecord: EditMedicalRecordRequest = new EditMedicalRecordRequest();

  clickEditMedicalRecord(medicalRecord: any): void {
    this.editingMedicalRecord = { ...medicalRecord };
    console.log(this.editingMedicalRecord)
    this.editingMedicalRecord.medicalRecordId = medicalRecord.id;
    this.isEditFormVisible = true;
  }

  closeEditForm(): void {
    this.isEditFormVisible = false;
  }

  fetchMedicalRecords(): void {
    this.fetchMedicalRecordsOfPatientByPatientID(this.patientId, this.currentPageMR, this.pageSizeMR);
  }

  onUpdate(): void {
    this.loading = true;
    this.editingMedicalRecord.patientId = this.patientId;
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
            this.closeEditForm();
            this.fetchMedicalRecordsOfPatientByPatientID(this.patientId, 1, this.pageSizeMR)
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
