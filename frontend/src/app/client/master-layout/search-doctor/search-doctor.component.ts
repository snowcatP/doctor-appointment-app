import { Component, ViewEncapsulation, OnInit,ViewChild} from '@angular/core';
import { DoctorService } from '../../../services/doctor.service';
import { MatPaginator } from '@angular/material/paginator';
import { Router } from '@angular/router';
@Component({
  selector: 'app-search-doctor',
  templateUrl: './search-doctor.component.html',
  styleUrls: ['./search-doctor.component.css'],
})
export class SearchDoctorComponent implements OnInit {
  doctors: any[] = [];
  totalDoctors: number = 0; // Tổng số lượng bác sĩ
  pageSize: number = 10; // Số lượng item mỗi trang
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  constructor(private doctorService: DoctorService, private router: Router) {}

  ngOnInit(): void {
    this.fetchDoctors(1, this.pageSize);
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
        }
      },
      (error) => {
        console.error('Error fetching doctors', error);
      }
    );
  }
  

   // Khi paginator thay đổi trang
   handlePageEvent(event: any): void {
    this.fetchDoctors(event.pageIndex + 1, event.pageSize);
  }

  viewDoctorProfile(doctorId: number): void {
    this.router.navigate(['/doctor-profile/', doctorId]); // Điều hướng với id của bác sĩ
  }
  
}
