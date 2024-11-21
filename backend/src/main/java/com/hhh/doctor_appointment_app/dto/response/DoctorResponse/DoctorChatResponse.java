package com.hhh.doctor_appointment_app.dto.response.DoctorResponse;

import com.hhh.doctor_appointment_app.entity.Specialty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorChatResponse {
    private Long id;
    private String fullName;
    private String email;
    private Specialty specialty;
    private String avatarFilePath;
}
