package com.hhh.doctor_appointment_app.dto.mapper;
import com.hhh.doctor_appointment_app.dto.response.ChatResponse.ChatMessageResponse;
import com.hhh.doctor_appointment_app.entity.ChatMessage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper {

    ChatMessageResponse toChatMessageResponse(ChatMessage chatMessage);

    List<ChatMessageResponse> toChatMessageResponseList(List<ChatMessage> chatMessageList);
}
