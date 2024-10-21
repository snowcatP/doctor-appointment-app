package com.hhh.doctor_appointment_app.state;

import com.hhh.doctor_appointment_app.entity.Appointment;

public class CancelledState implements AppointmentState{
    @Override
    public void next(Appointment appointment) {

    }

    @Override
    public String getStatus() {
        return "Cancelled";
    }
}
