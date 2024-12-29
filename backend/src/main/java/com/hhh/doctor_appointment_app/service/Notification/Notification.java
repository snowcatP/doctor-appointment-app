package com.hhh.doctor_appointment_app.service.Notification;

import com.hhh.doctor_appointment_app.enums.AppointmentStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Notification {
    private AppointmentStatus status;
    private String message;
    private Long appointmentId;
}
