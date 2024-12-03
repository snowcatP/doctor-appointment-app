import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DoctorService } from '../../../../core/services/doctor.service';
import { FeedbackService } from '../../../../core/services/feedback.service';
import { FeedbackRequest, ApiResponse } from '../../../../core/models/feedback.model';
import { MessageService } from 'primeng/api';
import { Router } from '@angular/router';
import * as CryptoJS from 'crypto-js';
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

  activeTab: string = 'overview'; // Biến để lưu trạng thái của tab hiện tại

  // Validation flags
  isRatingValid: boolean = true;
  isCommentValid: boolean = true;


  setActiveTab(tab: string) {
    this.activeTab = tab;
  }

  constructor(
    private route: ActivatedRoute, 
    private doctorService: DoctorService,
    private feedbackService: FeedbackService,
    private messageService: MessageService,
    private router: Router
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
  

  onSubmitReview() {
     // Validate rating and comment
     this.isRatingValid = this.feedbackRequest.rating > 0;
     this.isCommentValid = this.feedbackRequest.comment.trim().length > 0;
 
     if (!this.isRatingValid || !this.isCommentValid) {
       this.messageService.add({
         key: 'messageToast',
         severity: 'error',
         summary: 'Error',
         detail: 'Please provide a rating and a comment.',
       });
       return;
     }

    this.feedbackService.createFeedbackForDoctorByPatient(this.feedbackRequest).subscribe(
      (response: ApiResponse) => {
        if (response.statusCode === 200) {
          console.log(response);
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

  bookAppointmentNow(doctorId: number): void {
    const secretKey = '28a57933ee4343d000fe4d347ac74dc96ea35c699c1de470b68c7741b26a513f'; // Khóa bí mật
    const encryptedId = CryptoJS.AES.encrypt(doctorId.toString(), secretKey).toString();
    this.router.navigate(['/booking/appointment'], { queryParams: { doctorId: encryptedId } });
  }
}

