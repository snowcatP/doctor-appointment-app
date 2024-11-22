package com.hhh.doctor_appointment_app.dto.response.PatientResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientChatResponse {
    private Long id;
    private String fullName;
    private String email;
    private String avatarFilePath;
}
