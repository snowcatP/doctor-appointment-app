package com.hhh.doctor_appointment_app.dto.request.ChatbotRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatbotRequest {
    private String prompt;
}
