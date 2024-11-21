package com.hhh.doctor_appointment_app.dto.response.ChatResponse;

import com.hhh.doctor_appointment_app.dto.response.ConversationResponse.ConversationResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageResponse {
    private Long id;
    private String message;
    private String sender;
    private ConversationResponse conversation;
    private Date timeStamp;
}
