import { Component , Inject } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle,
} from '@angular/material/dialog';
import { ReplyFeedbackRequest, ApiResponse } from '../../../../../core/models/feedback.model';
import { FeedbackService } from '../../../../../core/services/feedback.service';
import { MessageService } from 'primeng/api';
@Component({
  selector: 'app-reply-dialog',
  templateUrl: './reply-dialog.component.html',
  styleUrl: './reply-dialog.component.css'
})
export class ReplyDialogComponent {
  feedbackRequest = {
    comment: '',
  };

  replyFeedback: ReplyFeedbackRequest = new ReplyFeedbackRequest();


  constructor(
    public dialogRef: MatDialogRef<ReplyDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private feedbackService: FeedbackService,
    private messageService: MessageService,
  ) {
    console.log('Received feedback data:', data.feedback);
    this.replyFeedback.doctorId = data.feedback.doctorResponse.id;
    this.replyFeedback.patientId = data.feedback.patientResponse.id;
    this.replyFeedback.replyCommentID = data.feedback.id;
  }

  onSubmitReview(): void {
    // Send feedbackRequest data back to the parent component
    // this.dialogRef.close(this.feedbackRequest);
    this.replyFeedback.comment = this.feedbackRequest.comment;
    this.feedbackService.replyFeedbackForPatientByDoctor(this.replyFeedback).subscribe(
      (response: ApiResponse) => {
        if (response.statusCode === 200) {
          this.messageService.add({
            key: 'messageToast',
            severity: 'success',
            summary: 'Success',
            detail: response.message || 'Reply review successfully',
          });
          setTimeout(() => {
            this.dialogRef.close(response);
              }, 1000);
        } else {
          this.messageService.add({
            key: 'messageToast',
            severity: 'error',
            summary: 'Error',
            detail: response.message || 'Failed to reply review',
          });
        }
      },
      (error) => {
        console.error('Failed to reply review', error);
        this.messageService.add({
          key: 'messageToast',
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to reply review due to server error',
        });
      }
    );

  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
