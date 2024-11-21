package com.hhh.doctor_appointment_app.dto.response.MedicalRecordResponse;

import com.hhh.doctor_appointment_app.dto.response.AppointmentResponse.AppointmentResponse;
import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorResponse;
import com.hhh.doctor_appointment_app.dto.response.NurseResponse.NurseResponse;
import com.hhh.doctor_appointment_app.repository.NurseRepository;
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
    private String bloodType; // Nhom mau

    private Integer heartRate; // Nhip tim

    private String description; // Symptoms recorded from the patient. ( Cac trieu chung duoc ghi nhan tu benh nhan)

    private String diagnosis; // Accurate diagnosis by doctor. ( Chan doan cua bac si)

    private String allergies; // Record patient allergies. ( Ghi nhan cac trieu chung di ung cua benh nhan)

    private String prescription; // Prescription or treatment instructions.( Don thuoc hoac huong dan dieu tri)

    private String treatmentPlan; //Ke hoach dieu tri

    private String note;
    private String filePath;
    private LocalDateTime dateCreated;
    private LocalDateTime lastModified;
    private DoctorResponse doctorResponse;
    private AppointmentResponse appointmentResponse;
    private NurseResponse nurseResponse;
}
