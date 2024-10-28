import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DoctorService } from '../../../../core/services/doctor.service';
import { FeedbackService } from '../../../../core/services/feedback.service';

@Component({
  selector: 'app-doctor-profile',
  templateUrl: './doctor-profile.component.html',
  styleUrl: './doctor-profile.component.css'
})
export class DoctorProfileComponent {
  doctorId!: number;  // ID của bác sĩ
  doctorDetails: any; // Thông tin chi tiết của bác sĩ
  feedbackList: any[] = []; // Feedback list for the doctor
  numberOfFeedbacks!: number;
  constructor(
    private route: ActivatedRoute, 
    private doctorService: DoctorService,
    private feedbackService: FeedbackService
  ) {}

  ngOnInit(): void {
    // Lấy id từ URL
    this.route.paramMap.subscribe(params => {
      this.doctorId = +params.get('id')!; // Lấy id của bác sĩ từ route parameter

      // Gọi API để lấy thông tin chi tiết của bác sĩ
      this.fetchDoctorDetails(this.doctorId);
      // Fetch feedbacks for the doctor
      this.fetchFeedbacks(this.doctorId);
    });
  }

  fetchDoctorDetails(id: number): void {
    this.doctorService.getDoctorDetail(id).subscribe(
      (response) => {
        if (response.statusCode === 200) {
          this.doctorDetails = response.data;
        }
      },
      (error) => {
        console.error('Error fetching doctor details', error);
      }
    );
  }

  fetchFeedbacks(doctorId: number): void {
    this.feedbackService.getFeedbacksOfDoctorByDoctorID(doctorId).subscribe(
      (response) => {
        if (response.statusCode === 200) {
          this.feedbackList = response.data;
          this.numberOfFeedbacks = response.totalPage;
        }
      },
      (error) => {
        console.error('Error fetching feedbacks', error);
      }
    );
  }
  activeTab: string = 'overview'; // Biến để lưu trạng thái của tab hiện tại
  setActiveTab(tab: string) {
    this.activeTab = tab;
  }
}

