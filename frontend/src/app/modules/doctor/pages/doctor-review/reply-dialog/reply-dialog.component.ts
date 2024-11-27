import { Component , Inject, Input, Output, OnInit, EventEmitter } from '@angular/core';
import { ReplyFeedbackRequest, ApiResponse } from '../../../../../core/models/feedback.model';
import { FeedbackService } from '../../../../../core/services/feedback.service';
import { MessageService } from 'primeng/api';
@Component({
  selector: 'app-reply-dialog',
  templateUrl: './reply-dialog.component.html',
  styleUrl: './reply-dialog.component.css'
})
export class ReplyDialogComponent implements OnInit{
  @Input() data: any; // Nhận data từ component cha
  @Output() onClose = new EventEmitter<ApiResponse>();

  feedbackRequest = {
    comment: '',
  };

  isCommentValid: boolean = true;

  replyFeedback: ReplyFeedbackRequest = new ReplyFeedbackRequest();

  visible: boolean = false;

  ngOnInit(): void {
    this.visible = true;
    this.replyFeedback.doctorId = this.data.doctorResponse.id;
    this.replyFeedback.patientId = this.data.patientResponse.id;
    this.replyFeedback.replyCommentID = this.data.id;
  }

  constructor(
    private feedbackService: FeedbackService,
    private messageService: MessageService
  ) {
  }

  onSubmitReview(): void {
    // Send feedbackRequest data back to the parent component
    // this.dialogRef.close(this.feedbackRequest);

    this.isCommentValid = this.feedbackRequest.comment.trim().length > 0;
 
     if (!this.isCommentValid) {
       this.messageService.add({
         key: 'messageToast',
         severity: 'error',
         summary: 'Error',
         detail: 'Please provide a comment.',
       });
       return;
     }

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
            this.visible = false; // Close dialog
            this.onClose.emit(response);
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
    this.onClose.emit();
  }

}
