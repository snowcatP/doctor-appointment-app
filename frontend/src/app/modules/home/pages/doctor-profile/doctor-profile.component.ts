import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DoctorService } from '../../../../core/services/doctor.service';
import { FeedbackService } from '../../../../core/services/feedback.service';
import { FeedbackRequest, ApiResponse } from '../../../../core/models/feedback.model';
import { MessageService } from 'primeng/api';
@Component({
  selector: 'app-doctor-profile',
  templateUrl: './doctor-profile.component.html',
  styleUrl: './doctor-profile.component.css'
})
export class DoctorProfileComponent {
  doctorId!: number; 
  doctorDetails: any;
  feedbackList: any[] = []; // Feedback list for the doctor
  numberOfFeedbacks!: number;

  feedbackRequest: FeedbackRequest = new FeedbackRequest();

  constructor(
    private route: ActivatedRoute, 
    private doctorService: DoctorService,
    private feedbackService: FeedbackService,
    private messageService: MessageService,
  ) {}

  ngOnInit(): void {
    window.scrollTo(0, 0); // Đảm bảo trang được cuộn lên đầu mỗi khi vào component
    // Lấy id từ URL
    this.route.paramMap.subscribe(params => {
      this.doctorId = +params.get('id')!; // Lấy id của bác sĩ từ route parameter
      this.feedbackRequest.doctorId = this.doctorId;
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

  onSubmitReview() {
    this.feedbackService.createFeedbackForDoctorByPatient(this.feedbackRequest).subscribe(
      (response: ApiResponse) => {
        if (response.statusCode === 200) {
          this.messageService.add({
            key: 'messageToast',
            severity: 'success',
            summary: 'Success',
            detail: response.message || 'Review created successfully',
          });
          // Fetch feedbacks for the doctor
          this.fetchFeedbacks(this.doctorId);
        } else {
          this.messageService.add({
            key: 'messageToast',
            severity: 'error',
            summary: 'Error',
            detail: response.message || 'Failed to create review',
          });
        }
      },
      (error) => {
        console.error('Failed to create review', error);
        this.messageService.add({
          key: 'messageToast',
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to create review due to server error',
        });
      }
    );
  }
}

