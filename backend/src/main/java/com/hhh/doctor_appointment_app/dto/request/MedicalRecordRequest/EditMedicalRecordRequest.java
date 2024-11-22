package com.hhh.doctor_appointment_app.dto.request.MedicalRecordRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditMedicalRecordRequest {
    private String bloodType; // Nhom mau

    private Integer heartRate; // Nhip tim

    private String description; // Symptoms recorded from the patient. ( Cac trieu chung duoc ghi nhan tu benh nhan)

    private String diagnosis; // Accurate diagnosis by doctor. ( Chan doan cua bac si)

    private String allergies; // Record patient allergies. ( Ghi nhan cac trieu chung di ung cua benh nhan)

    private String prescription; // Prescription or treatment instructions.( Don thuoc hoac huong dan dieu tri)

    private String treatmentPlan; //Ke hoach dieu tri

    private String note;
    private String filePath;
    private Long patientId;
    private Long medicalRecordId;
}
