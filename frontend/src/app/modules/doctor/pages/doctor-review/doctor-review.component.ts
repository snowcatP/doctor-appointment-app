import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { FeedbackService } from '../../../../core/services/feedback.service';
import { ReplyDialogComponent } from './reply-dialog/reply-dialog.component';
import { ApiResponse } from '../../../../core/models/feedback.model';
@Component({
  selector: 'app-doctor-review',
  templateUrl: './doctor-review.component.html',
  styleUrl: './doctor-review.component.css'
})
export class DoctorReviewComponent {
  feedbacks: any[] = [];
  totalFeedbacks: number = 0;
  pageSize: number = 5;
  currentPage: number = 1;

  isReplyDialogVisible: boolean = false;
  replyFeedbackData: any;

  constructor(private feedbackService: FeedbackService,
    private router: Router) { }

  ngOnInit(): void {
    this.fetchFeedbackOfDoctorByDoctorEmail(1, this.pageSize);
  }

  fetchFeedbackOfDoctorByDoctorEmail(page: number, pageSize: number): void {
    this.feedbackService.getFeedbackOfDoctorByDoctorEmail(page, pageSize).subscribe(
      (response) => {
        if (response.statusCode === 200) {
          this.feedbacks = response.data;
          this.totalFeedbacks = response.totalPage * pageSize;
        }
      },
      (error) => {
        console.error('Error fetching feedback', error);
      }
    );
  }


  handlePageEvent(event: any): void {
    this.currentPage = (event.page + 1);
    this.pageSize = event.rows;
    this.fetchFeedbackOfDoctorByDoctorEmail(this.currentPage, this.pageSize);
  }

  onPageSizeChange(): void {
    this.currentPage = 1;
    this.fetchFeedbackOfDoctorByDoctorEmail(this.currentPage, this.pageSize);
  }

  openReplyDialog(feedback: any, event: Event): void {
    event.preventDefault();
    console.log("Feedback data:", feedback);
    this.replyFeedbackData = feedback; // Lưu dữ liệu phản hồi để truyền vào dialog
    this.isReplyDialogVisible = true; // Hiển thị dialog
  }

  onDialogClose(result: ApiResponse | undefined): void {
    this.isReplyDialogVisible = false; // Ẩn dialog
    if (result) {
      console.log("Dialog closed with result:", result);
      this.fetchFeedbackOfDoctorByDoctorEmail(1, this.pageSize); // Cập nhật lại danh sách phản hồi
    }
  }
}
