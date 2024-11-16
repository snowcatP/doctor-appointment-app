package com.hhh.doctor_appointment_app.dto.response.MedicalRecordResponse;

import com.hhh.doctor_appointment_app.dto.response.AppointmentResponse.AppointmentResponse;
import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecordResponse {
    private Long id;
    private String emailPatient;
    private String description;
    private String filePath;
    private LocalDateTime dateCreated;
    private LocalDateTime lastModified;
    private DoctorResponse doctorResponse;
    private AppointmentResponse appointmentResponse;
}
