package com.hhh.doctor_appointment_app.dto.request.AppointmentRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AppointmentByGuestRequest {
    @Size(max = 50, message = "Full name must not exceed 50 characters")
    private String fullName;

    private String phone;

    @Email(message = "Email is invalid")
    private String email;

    private String reason;

    private Date dateBooking;

    private String bookingHour;

    private Long doctorId;
}
