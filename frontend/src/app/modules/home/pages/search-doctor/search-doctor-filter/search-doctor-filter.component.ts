import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { SpecialtyService } from '../../../../../core/services/specialty.service';
import { DoctorService } from '../../../../../core/services/doctor.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-search-doctor-filter',
  templateUrl: './search-doctor-filter.component.html',
  styleUrl: './search-doctor-filter.component.css'
})
export class SearchDoctorFilterComponent implements OnInit{
  specialties: any[] = [];
  searchRequest: any = {
    keyword: '',
    specialtyId: [],
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
      this.searchRequest.specialtyId.push(specialtyId);
      console.log( this.searchRequest.specialtyId)
    } else {
      this.searchRequest.specialtyId = this.searchRequest.specialtyId.filter((id: number) => id !== specialtyId);
    }
  }
  
}

