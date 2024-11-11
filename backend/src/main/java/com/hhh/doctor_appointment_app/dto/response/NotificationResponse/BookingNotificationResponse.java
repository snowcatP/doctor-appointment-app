package com.hhh.doctor_appointment_app.dto.response.NotificationResponse;

import com.hhh.doctor_appointment_app.entity.Doctor;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingNotificationResponse {
    private Long appointmentId;
    private Doctor doctor;
    private Date dateBooking;
    private String bookingHour;
}
