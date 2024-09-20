package com.hhh.doctor_appointment_app.entity;

import com.hhh.doctor_appointment_app.entity.Enum.Specialty;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "doctor")
public class Doctor extends User{
    @Column(name = "specialty")
    @Enumerated(EnumType.STRING)
    private Specialty specialty;

    @OneToMany(mappedBy = "doctor",cascade = CascadeType.ALL)
    private List<MedicalRecord> medicalRecordList = new ArrayList<>();

    @OneToMany(mappedBy = "doctor",cascade = CascadeType.ALL)
    private List<Appointment> appointmentList = new ArrayList<>();

    @OneToMany(mappedBy = "doctor",cascade = CascadeType.ALL)
    private List<Feedback> feedbackList = new ArrayList<>();
}
