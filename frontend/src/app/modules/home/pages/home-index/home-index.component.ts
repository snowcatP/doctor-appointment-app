import { Component } from '@angular/core';
import { DoctorService } from '../../../../core/services/doctor.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home-index',
  templateUrl: './home-index.component.html',
  styleUrl: './home-index.component.css'
})
export class HomeIndexComponent {
  ngOnInit(): void {
    this.fetchTopRatingDoctors();
  }
  specialties: any[] = [
    {
      image: 'assets/images/specialities/specialities-01.png',
      name: 'Urology',
    },
    {
      image: 'assets/images/specialities/specialities-02.png',
      name: 'Neurology',
    },
    {
      image: 'assets/images/specialities/specialities-03.png',
      name: 'Orthopedic',
    },
    {
      image: 'assets/images/specialities/specialities-04.png',
      name: 'Cardiologist',
    },
    {
      image: 'assets/images/specialities/specialities-05.png',
      name: 'Dentist',
    },
  ];
  features: any[] = [
    { image: 'assets/images/features/feature-01.jpg', name: 'Patient Ward' },
    { image: 'assets/images/features/feature-02.jpg', name: 'Test Room' },
    { image: 'assets/images/features/feature-03.jpg', name: 'ICU' },
    { image: 'assets/images/features/feature-04.jpg', name: 'Laboratory' },
    { image: 'assets/images/features/feature-05.jpg', name: 'Operation' },
    { image: 'assets/images/features/feature-06.jpg', name: 'Medical' },
  ];

  topRatingDoctors: any[] = [];
  constructor(private doctorService: DoctorService, private router: Router) {}

  fetchTopRatingDoctors(): void {
    this.doctorService.getTopRatingDoctors().subscribe(
      (response) => {
        if (response.statusCode === 200) {
          this.topRatingDoctors = response.data;
        }
      },
      (error) => {
        console.error('Error fetching doctors');
      }
    );
  }

  viewDoctorProfile(doctorId: number): void {
    this.router.navigate(['/doctor-profile/', doctorId]); // Điều hướng với id của bác sĩ
  }

  responsiveOptions = [
    {
      breakpoint: '1024px',
      numVisible: 3,
      numScroll: 3,
    },
    {
      breakpoint: '768px',
      numVisible: 2,
      numScroll: 2,
    },
    {
      breakpoint: '560px',
      numVisible: 1,
      numScroll: 1,
    },
  ];
}
