import { Component, OnDestroy } from '@angular/core';
import { DoctorService } from '../../../../core/services/doctor.service';
import { Router } from '@angular/router';
import { ReferenceCodeRequest } from '../../../../core/models/appointment.model';
import { AppointmentService } from '../../../../core/services/appointment.service';
import * as CryptoJS from 'crypto-js';
import { Subscription } from 'rxjs';
@Component({
  selector: 'app-home-index',
  templateUrl: './home-index.component.html',
  styleUrl: './home-index.component.css'
})
export class HomeIndexComponent implements OnDestroy{

  referenceCode: string;
  referenceCodeRequest: ReferenceCodeRequest;
  visible: boolean = false;

  appointment!: any;
  private subscription: Subscription = new Subscription();
  ngOnInit(): void {
    this.subscription.add(
      this.appointmentService.dialogVisible$.subscribe(
        visible => this.visible = visible
      )
    );

    this.subscription.add(
      this.appointmentService.appointmentData$.subscribe(
        data => this.appointment = data
      )
    );
    this.visible = false;
    this.fetchTopRatingDoctors();
  }

  ngOnDestroy(){
    this.subscription.unsubscribe();
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
  constructor(private doctorService: DoctorService, 
    private router: Router,
    private appointmentService: AppointmentService) {}

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

  fetchGetAppointmentByReferenceCode(): void {
    this.referenceCodeRequest = { referenceCode: this.referenceCode };
    console.log(this.referenceCodeRequest)
    this.appointmentService.getAppointmentByReferenceCode(this.referenceCodeRequest).subscribe(
      (response) => {
        if (response.statusCode === 200) {
          this.appointment = response.data;
          this.visible = true;
        } else{
          console.log(response)
        }
      },
      (error) => {
        console.error('Error fetching appointment', error);
      }
    );
  }

  bookAppointmentNow(doctorId: number): void {
    const secretKey = '28a57933ee4343d000fe4d347ac74dc96ea35c699c1de470b68c7741b26a513f'; // Khóa bí mật
    const encryptedId = CryptoJS.AES.encrypt(doctorId.toString(), secretKey).toString();
    this.router.navigate(['/booking/appointment'], { queryParams: { doctorId: encryptedId } });
  }
}
