package com.hhh.doctor_appointment_app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bloodType; // Nhom mau

    private Integer heartRate; // Nhip tim

    private String description; // Symptoms recorded from the patient. ( Cac trieu chung duoc ghi nhan tu benh nhan)

    private String diagnosis; // Accurate diagnosis by doctor. ( Chan doan cua bac si)

    private String allergies; // Record patient allergies. ( Ghi nhan cac trieu chung di ung cua benh nhan)

    private String prescription; // Prescription or treatment instructions.( Don thuoc hoac huong dan dieu tri)

    private String treatmentPlan; //Ke hoach dieu tri

    private String note;

    private String filePath;

    @CreatedDate
    private LocalDateTime dateCreated;

    @LastModifiedDate
    private LocalDateTime lastModified;

    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "doctor_id")
    private Doctor doctorModified;

    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "nurse_id")
    private Nurse nurse;

    @OneToOne()
    @JsonIgnore
    private Appointment appointment;
}
