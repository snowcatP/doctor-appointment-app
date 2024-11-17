package com.hhh.doctor_appointment_app.dto.request.AppointmentRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAppointmentForReschedulingRequest {
    private String doctorEmail;
}
