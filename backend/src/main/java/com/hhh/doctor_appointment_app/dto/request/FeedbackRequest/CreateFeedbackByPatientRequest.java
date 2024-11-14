package com.hhh.doctor_appointment_app.dto.request.FeedbackRequest;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CreateFeedbackByPatientRequest {
    @Size(max = 100, message = "Comment must not exceed 100 characters")
    private String comment;

    private double rating;

    private Long doctorId;
}
