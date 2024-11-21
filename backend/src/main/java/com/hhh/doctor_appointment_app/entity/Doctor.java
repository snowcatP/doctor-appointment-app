package com.hhh.doctor_appointment_app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Doctor{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User profile;

    private double averageRating;

    private String schedule;    // MONDAY,TUESDAY

    @ManyToOne
    @JoinColumn(name = "specialty_id")
    @JsonIgnore
    private Specialty specialty;

    @OneToMany(mappedBy = "doctorModified",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<MedicalRecord> medicalRecordList = new ArrayList<>();

    @OneToMany(mappedBy = "doctor",cascade = CascadeType.ALL)
    private List<Appointment> appointmentList = new ArrayList<>();

    @OneToMany(mappedBy = "doctor",cascade = CascadeType.ALL)
    private List<Feedback> feedbackList = new ArrayList<>();
}
