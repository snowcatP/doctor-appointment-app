import { Component } from '@angular/core';
import { DoctorService } from '../../../../core/services/doctor.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-doctor-mypatient',
  templateUrl: './doctor-mypatient.component.html',
  styleUrl: './doctor-mypatient.component.css'
})
export class DoctorMypatientComponent {
  patients: any[] = [];
  totalPatients: number = 0;
  pageSize: number = 8;
  currentPage: number = 1;
  searchPatientName: string = '';
  totalSize: number = 0;
  constructor(private doctorService: DoctorService,
    private router: Router,
  ) { }

  ngOnInit(): void {
    this.fetchGetAllPatientOfDoctor(1, this.pageSize, this.searchPatientName);
  }

  fetchGetAllPatientOfDoctor(page: number, pageSize: number, patientName: string): void {
    this.doctorService.getAllPatientsOfDoctor(page, pageSize,patientName).subscribe(
      (response) => {
        if (response.statusCode === 200) {
          console.log(response)
          this.patients = response.data;
          this.totalPatients = response.totalPage * pageSize;
          this.totalSize = response.totalSize;
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
    this.fetchGetAllPatientOfDoctor(this.currentPage, this.pageSize, this.searchPatientName);
  }

  onPageSizeChange(): void {
    this.currentPage = 1;
    this.fetchGetAllPatientOfDoctor(this.currentPage, this.pageSize, this.searchPatientName);
  }

  onSearchChange(): void {
    this.currentPage = 1;
    this.fetchGetAllPatientOfDoctor(this.currentPage, this.pageSize, this.searchPatientName);
  }

  calculateAge(dateOfBirth: string): number {
    const birthDate = new Date(dateOfBirth);
    const ageDiffMs = Date.now() - birthDate.getTime();
    const ageDate = new Date(ageDiffMs);
    return Math.abs(ageDate.getUTCFullYear() - 1970);
  }
  
  // Click view Patient Profile
  viewPatientProfile(patientId: string): void {
    this.router.navigate([`/doctor/patient-profile`, patientId]);
  }
}
