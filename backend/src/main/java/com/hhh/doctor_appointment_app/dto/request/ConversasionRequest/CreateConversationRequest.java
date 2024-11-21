package com.hhh.doctor_appointment_app.dto.request.ConversasionRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateConversationRequest {
    private String doctorEmail;
    private String patientEmail;
}
