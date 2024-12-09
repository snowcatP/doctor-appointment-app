package com.hhh.doctor_appointment_app.dto.request.MedicalRecordRequest;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditMedicalRecordRequest {
    //Information filled by Nurse
    private String bloodType;
    private Integer heartRate;
    private Double temperature; // Nhiet do co the
    private Double weight; // Can nang
    private Double height; // Chieu cao
    @Size(max = 100, message = "Description must not exceed 100 characters")
    private String description; // Symptoms recorded from the patient.
    private String allergies; // Record patient allergies. ( Ghi nhan cac trieu chung di ung cua benh nhan)

    //Information filled by Doctor
    @Size(max = 100, message = "Diagnosis must not exceed 50 characters")
    private String diagnosis; // Accurate diagnosis by doctor. ( Chan doan cua bac si)
    @Size(max = 100, message = "Prescription must not exceed 50 characters")
    private String prescription; // Prescription or treatment instructions.( Don thuoc hoac huong dan dieu tri)
    @Size(max = 100, message = "Treatment plan must not exceed 50 characters")
    private String treatmentPlan;
    @Size(max = 100, message = "Note must not exceed 50 characters")
    private String note;
    private String filePath;
    private Long patientId;
    private Long medicalRecordId;
    private String emailPatient;
}
