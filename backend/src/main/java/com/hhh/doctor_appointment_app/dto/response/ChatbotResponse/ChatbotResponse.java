package com.hhh.doctor_appointment_app.dto.response.ChatbotResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatbotResponse {
    private String result;
    private String sender;
}
