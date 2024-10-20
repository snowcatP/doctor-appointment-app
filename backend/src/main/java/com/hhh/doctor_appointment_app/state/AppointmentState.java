package com.hhh.doctor_appointment_app.state;

import com.hhh.doctor_appointment_app.entity.Appointment;

public interface AppointmentState {
    void next(Appointment appointment);
    String getStatus();
}
