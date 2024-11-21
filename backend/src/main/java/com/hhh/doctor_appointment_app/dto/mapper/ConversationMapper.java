package com.hhh.doctor_appointment_app.dto.mapper;

import com.hhh.doctor_appointment_app.dto.response.ConversationResponse.ConversationResponse;
import com.hhh.doctor_appointment_app.entity.Conversation;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConversationMapper {

    ConversationResponse toResponse(Conversation conversation);

    List<ConversationResponse> toListResponse(List<Conversation> conversations);
}
