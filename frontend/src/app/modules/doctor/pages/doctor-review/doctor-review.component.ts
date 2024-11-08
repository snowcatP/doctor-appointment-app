import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { FeedbackService } from '../../../../core/services/feedback.service';
import { ReplyDialogComponent } from './reply-dialog/reply-dialog.component';
import { MatDialog } from '@angular/material/dialog';
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

  constructor(private feedbackService: FeedbackService,
    private router: Router,
    public dialog: MatDialog) { }

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
        console.error('Error fetching appointments', error);
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
    const dialogRef = this.dialog.open(ReplyDialogComponent, {
      width: '500px',
      data: { feedback} // truyền dữ liệu bác sĩ nếu cần thiết
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.fetchFeedbackOfDoctorByDoctorEmail(1, this.pageSize);
      }
    });
  }
}
