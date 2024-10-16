import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  ngOnInit(): void {}
  specialties: any[] = [
    { image: 'assets/images/specialities/specialities-01.png', name: 'Urology' },
    { image: 'assets/images/specialities/specialities-02.png', name: 'Neurology' },
    { image: 'assets/images/specialities/specialities-03.png', name: 'Orthopedic' },
    { image: 'assets/images/specialities/specialities-04.png', name: 'Cardiologist' },
    { image: 'assets/images/specialities/specialities-05.png', name: 'Dentist' }
    
  ];
  features: any[] = [
    { image: 'assets/images/features/feature-01.jpg', name: 'Patient Ward' },
    { image: 'assets/images/features/feature-02.jpg', name: 'Test Room' },
    { image: 'assets/images/features/feature-03.jpg', name: 'ICU' },
    { image: 'assets/images/features/feature-04.jpg', name: 'Laboratory' },
    { image: 'assets/images/features/feature-05.jpg', name: 'Operation' },
    { image: 'assets/images/features/feature-06.jpg', name: 'Medical' }
  ];

}