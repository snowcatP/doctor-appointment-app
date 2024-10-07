package com.hhh.doctor_appointment_app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hhh.doctor_appointment_app.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;

import javax.print.Doc;
import java.util.Date;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "gender")
    private boolean gender;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email",  nullable = false, unique = true)
    private String email;

    @Column(name = "dateOfBirth")
    private Date dateOfBirth;

    @Column(name = "reason")
    private String reason;

    private Date dateBooking;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AppointmentStatus appointmentStatus;

    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
}
