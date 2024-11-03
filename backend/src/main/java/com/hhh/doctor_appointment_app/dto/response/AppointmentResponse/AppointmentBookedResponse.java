package com.hhh.doctor_appointment_app.dto.response.AppointmentResponse;

import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentBookedResponse {
    private Long id;
    private Date dateBooking;
    private String bookingHour;
    private Doctor doctor;
    private Patient patient;
}
