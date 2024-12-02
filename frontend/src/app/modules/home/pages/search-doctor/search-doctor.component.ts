import { Component, OnInit } from '@angular/core';
import { DoctorService } from '../../../../core/services/doctor.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-search-doctor',
  templateUrl: './search-doctor.component.html',
  styleUrl: './search-doctor.component.css'
})
export class SearchDoctorComponent implements OnInit {
  doctors: any[] = [];
  totalDoctors: number = 0; 
  pageSize: number = 10;
  currentPage: number = 1;

  searchRequest: any = {
    keyword: '',
    specialtyId: [],
    gender: null
  };
  constructor(private doctorService: DoctorService, private router: Router) {}

  ngOnInit(): void {
    this.searchDoctors(this.currentPage, this.pageSize);
  }

  onDoctorsFound(doctors: any[]): void {
    this.doctors = doctors;
    this.totalDoctors = doctors.length; // Cập nhật tổng số bác sĩ
  }
  
  fetchDoctors(page: number, pageSize: number): void {
    this.doctorService.getDoctors(page, pageSize).subscribe(
      (response) => {
        if (response.statusCode === 200) {
          this.doctors = response.data;
          this.totalDoctors = response.totalPage * pageSize; // Tính tổng số lượng bác sĩ
        } else{
          console.log(response)
        }
      },
      (error) => {
        console.error('Error fetching doctors', error);
      }
    );
  }

  searchDoctors(page: number, pageSize: number): void {
    this.doctorService.searchDoctors(this.searchRequest, page, pageSize).subscribe(
      (response) => {
        if (response.statusCode === 200) {
          this.doctors = response.data;
          this.totalDoctors = response.totalPage * pageSize;
        }
      },
      (error) => {
        console.error('Error searching doctors', error);
      }
    );
  }
  

  handlePageEvent(event: any): void {
    this.currentPage = (event.page + 1);
    this.pageSize = event.rows;
    this.searchDoctors(this.currentPage, this.pageSize);
  }

  onPageSizeChange(): void {
    this.currentPage = 1;
    this.searchDoctors(this.currentPage, this.pageSize);
  }

  viewDoctorProfile(doctorId: number): void {
    this.router.navigate(['/doctor-profile/', doctorId]); // Điều hướng với id của bác sĩ
  }

  bookAppointmentNow(doctorId: number):void{
    this.doctorService.setDoctorId(doctorId);
    this.router.navigate(['/booking/appointment']);
  }
}

