package com.hhh.doctor_appointment_app.state;

import com.hhh.doctor_appointment_app.entity.Appointment;

public class InProgessState implements AppointmentState{
    @Override
    public void next(Appointment appointment) {
        appointment.setAppointmentState(new CompletedState());
    }

    @Override
    public String getStatus() {
        return "In_Progress";
    }
}
