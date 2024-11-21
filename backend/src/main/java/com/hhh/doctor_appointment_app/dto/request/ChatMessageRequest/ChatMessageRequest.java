package com.hhh.doctor_appointment_app.dto.request.ChatMessageRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageRequest {
    private Long conversationId;
    private String message;
    private String sender;
    private String doctorEmail;
    private String patientEmail;
}
