package com.hhh.doctor_appointment_app.dto.request.AppointmentRequest;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RescheduleWithDateRequest {
    private Date dateBooking;
    private String bookingHour;
}
