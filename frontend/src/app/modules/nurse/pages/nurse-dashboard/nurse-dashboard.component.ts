import { Component } from '@angular/core';
import { AppointmentService } from '../../../../core/services/appointment.service';
import { Router } from '@angular/router';
import { AddMedicalRecordRequest } from '../../../../core/models/medical-record.model';
@Component({
  selector: 'app-nurse-dashboard',
  templateUrl: './nurse-dashboard.component.html',
  styleUrl: './nurse-dashboard.component.css'
})
export class NurseDashboardComponent {
  appointments: any[] = [];
  totalAppointments: number = 0;
  pageSize: number = 10;
  currentPage: number = 1;
  searchPatientName: string = '';

  visible: boolean = false; // Control dialog visibility
  selectedAppointment: any = null; // Store selected appointment for dialog

  visibleMedicalRecord: boolean = false;
  selectedMedicalRecord: any = null;

  isFormVisible = false;

  selectedFile?: File;
  imagePreview: string | ArrayBuffer | null = null;
  fileName: string | null = null; // Biến để lưu tên tệp không phải là ảnh
  medicalRecordRequest: AddMedicalRecordRequest = new AddMedicalRecordRequest();
  loading: boolean = false; // Add loading state


  constructor(private appointmentService: AppointmentService,
    private router: Router,
  ) { }

  ngOnInit(): void {
    this.fetchGetAppointmentsForNurse(this.currentPage, this.pageSize);
  }

  fetchGetAppointmentsForNurse(page: number, pageSize: number): void {
    this.appointmentService.getAppointmentsForNurse(page, pageSize).subscribe(
      (response) => {
        if (response.statusCode === 200) {
          this.appointments = response.data;
          this.totalAppointments = response.totalPage * pageSize;
        } else{
          console.log(response)
        }
      },
      (error) => {
        console.error('Error fetching all patients', error);
      }
    );
  }


  handlePageEvent(event: any): void {
    this.currentPage = (event.page + 1);
    this.pageSize = event.rows;
    this.fetchGetAppointmentsForNurse(this.currentPage, this.pageSize);
  }

  onPageSizeChange(): void {
    this.currentPage = 1;
    this.fetchGetAppointmentsForNurse(this.currentPage, this.pageSize);
  }

  onSearchChange(): void {
    this.currentPage = 1;
    this.fetchGetAppointmentsForNurse(this.currentPage, this.pageSize);
  }

 
  // Open dialog and set selected appointment
  viewAppointmentDetails(appointment: any): void {
    this.selectedAppointment = appointment;
    this.visible = true;
  }

  openForm(appointment:any) {
    this.appointmentService.setAppointment(appointment);
    this.router.navigate(['/nurse/create-medical-record']);
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
    this.isFormVisible = false;
  }

  viewMedicalRecordDetails(medicalRecord: any): void {
    this.selectedMedicalRecord = medicalRecord;
    this.visibleMedicalRecord = true;
  }
}
