export class FeedbackRequest {
    comment: string ='';
    rating: number;
    doctorId: number;
}

export class ReplyFeedbackRequest{
    comment: string ='';
    replyCommentID: number;
    doctorId: number;
    patientId: number;
}
export interface ApiResponse {
    statusCode: number;
    message: string;
    data?: any; // Use the actual data type if known, such as `data?: UserProfile`
}