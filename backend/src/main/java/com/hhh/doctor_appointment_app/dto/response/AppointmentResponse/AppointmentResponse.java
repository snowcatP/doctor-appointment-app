package com.hhh.doctor_appointment_app.dto.response.AppointmentResponse;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AppointmentResponse {
    private Long id;

    private String fullname;

    private boolean gender;

    private String phone;

    private String email;

    private Date dateOfBirth;

    private String reason;

    private Date dateBooking;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus appointmentStatus;

    //tra ve thong tin cua bac si
    private User doctor;
}