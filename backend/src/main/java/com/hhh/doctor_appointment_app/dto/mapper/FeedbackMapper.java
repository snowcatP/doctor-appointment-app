package com.hhh.doctor_appointment_app.dto.mapper;

import com.hhh.doctor_appointment_app.dto.response.FeedbackResponse.FeedbackResponse;
import com.hhh.doctor_appointment_app.entity.Feedback;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {
    @Mapping(source = "feedback.id", target = "id")
    FeedbackResponse toResponse(Feedback feedback);
}
