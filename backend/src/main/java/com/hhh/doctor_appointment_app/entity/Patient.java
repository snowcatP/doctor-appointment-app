package com.hhh.doctor_appointment_app.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "patient")
public class Patient extends User {

    @OneToMany(mappedBy = "patient",cascade = CascadeType.ALL)
    private List<MedicalRecord> medicalRecordList = new ArrayList<>();

    @OneToMany(mappedBy = "patient",cascade = CascadeType.ALL)
    private List<Appointment> appointmentList = new ArrayList<>();

    @OneToMany(mappedBy = "patient",cascade = CascadeType.ALL)
    private List<Feedback> feedbackList = new ArrayList<>();

}
