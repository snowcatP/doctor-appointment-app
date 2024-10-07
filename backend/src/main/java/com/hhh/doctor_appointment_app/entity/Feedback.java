package com.hhh.doctor_appointment_app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


import java.util.Date;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    private Date dateComment;

    private double rating;

    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "patient_id")
    private Patient patient;
}
