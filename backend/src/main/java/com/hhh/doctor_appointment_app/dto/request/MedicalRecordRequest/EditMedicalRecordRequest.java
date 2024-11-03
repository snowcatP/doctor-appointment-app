package com.hhh.doctor_appointment_app.dto.request.MedicalRecordRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditMedicalRecordRequest {
    private String description;
    private String filePath;

    private Long patientId;
    private Long doctorId;
}
