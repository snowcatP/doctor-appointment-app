package com.hhh.doctor_appointment_app.dto.request.MedicalRecordRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditMedicalRecordRequest {
    private String description;
    private String filePath;

    private Long patient_id;
    private Long doctor_id;
}
