package com.hhh.doctor_appointment_app.dto.mapper;

import com.hhh.doctor_appointment_app.dto.response.ReplyFeedbackResponse.ReplyFeedbackResponse;
import com.hhh.doctor_appointment_app.entity.ReplyFeedback;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReplyFeedbackMapper {
    ReplyFeedbackResponse toResponse(ReplyFeedback replyFeedback);
}
