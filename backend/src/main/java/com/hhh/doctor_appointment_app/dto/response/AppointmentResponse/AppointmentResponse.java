package com.hhh.doctor_appointment_app.dto.response.AppointmentResponse;

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

    private String fullName;

    private String phone;

    private String email;

    private String reason;

    private Date dateBooking;

    private String bookingHour;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus appointmentStatus;

    private User doctor;
}
