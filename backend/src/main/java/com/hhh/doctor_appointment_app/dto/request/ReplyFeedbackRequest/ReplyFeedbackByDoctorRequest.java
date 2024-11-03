package com.hhh.doctor_appointment_app.dto.request.ReplyFeedbackRequest;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyFeedbackByDoctorRequest {
    @Size(max = 100, message = "Comment must not exceed 100 characters")
    private String comment;

    private Long replyCommentID;

    private Long doctorId;

    private Long patientId;
}
