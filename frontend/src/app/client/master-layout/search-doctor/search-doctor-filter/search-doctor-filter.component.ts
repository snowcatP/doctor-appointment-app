import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { SpecialtyService } from '../../../../services/specialty.service';
import { DoctorService } from '../../../../services/doctor.service';
import { MatPaginator } from '@angular/material/paginator';
import { Router } from '@angular/router';
@Component({
  selector: 'app-search-doctor-filter',
  templateUrl: './search-doctor-filter.component.html',
  styleUrl: './search-doctor-filter.component.css',
})
export class SearchDoctorFilterComponent implements OnInit{
  specialties: any[] = [];
  searchRequest: any = {
    keyword: '',
    specialty_Id: [],
    gender: null
  };

  @Output() doctorsFound: EventEmitter<any[]> = new EventEmitter<any[]>(); // Thêm EventEmitter


  constructor(
    private specialtyService: SpecialtyService,
    private doctorService: DoctorService, // Inject DoctorService
    private router: Router
  ) {}

  ngOnInit(): void {
    this.fetchListSpecialties();
  }

  fetchListSpecialties(): void {
    this.specialtyService.getListSpecialty().subscribe(
      (response) => {
        if (response.statusCode === 200) {
          this.specialties = response.data;
        }
      },
      (error) => {
        console.error('Error fetching specialties', error);
      }
    );
  }

  searchDoctors(): void {
    this.doctorService.searchDoctors(this.searchRequest, 1, 10).subscribe(
      (response) => {
        if (response.statusCode === 200) {
          this.doctorsFound.emit(response.data);
          console.log(this.doctorsFound);
        }
      },
      (error) => {
        console.error('Error searching doctors', error);
      }
    );
  }

  onGenderChange(gender: boolean, event: any): void {
    if (event.target.checked) {
      this.searchRequest.gender = gender;
      console.log(this.searchRequest.gender)
    } else {
      this.searchRequest.gender = null; // Bỏ chọn nếu checkbox không được chọn
    }
  }
  

  onSpecialtyChange(specialtyId: number, event: any): void {
    if (event.target.checked) {
      this.searchRequest.specialty_Id.push(specialtyId);
      console.log( this.searchRequest.specialty_Id)
    } else {
      this.searchRequest.specialty_Id = this.searchRequest.specialty_Id.filter((id: number) => id !== specialtyId);
    }
  }
  
}
