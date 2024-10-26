package com.hhh.doctor_appointment_app.dto.response.ReplyFeedbackResponse;

import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorResponse;
import com.hhh.doctor_appointment_app.dto.response.FeedbackResponse.FeedbackResponse;
import com.hhh.doctor_appointment_app.dto.response.PatientResponse.PatientResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ReplyFeedbackResponse {
    private Long id;
    private String comment;

    private Date dateComment;

    private DoctorResponse doctorResponse;

}
