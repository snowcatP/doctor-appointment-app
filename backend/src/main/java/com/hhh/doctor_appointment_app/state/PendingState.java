package com.hhh.doctor_appointment_app.state;

import com.hhh.doctor_appointment_app.entity.Appointment;

public class PendingState implements AppointmentState{
    @Override
    public void next(Appointment appointment) {
        appointment.setAppointmentState(new AcceptState());
    }

    @Override
    public String getStatus() {
        return "Pending";
    }
}
