package com.hhh.doctor_appointment_app.dto.response.ConversationResponse;

import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorChatResponse;
import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorResponse;
import com.hhh.doctor_appointment_app.dto.response.PatientResponse.PatientChatResponse;
import com.hhh.doctor_appointment_app.dto.response.PatientResponse.PatientResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConversationResponse {
    private Long id;
    private DoctorChatResponse doctor;
    private PatientChatResponse patient;
}
