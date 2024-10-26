package com.hhh.doctor_appointment_app.dto.response.FeedbackResponse;

import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorResponse;
import com.hhh.doctor_appointment_app.dto.response.PatientResponse.PatientResponse;
import com.hhh.doctor_appointment_app.dto.response.ReplyFeedbackResponse.ReplyFeedbackResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class FeedbackResponse {
    private Long id;

    private String comment;

    private Date dateComment;

    private double rating;

    private DoctorResponse doctorResponse;

    private PatientResponse patientResponse;

    private List<ReplyFeedbackResponse> replyFeedbackResponse;

}
