package com.hhh.doctor_appointment_app.state;

import com.hhh.doctor_appointment_app.entity.Appointment;

public class RescheduledState implements AppointmentState{
    @Override
    public void next(Appointment appointment) {
        appointment.setAppointmentState(new InProgessState());
    }

    @Override
    public String getStatus() {
        return "Rescheduled";
    }

    public void reschedule(Appointment appointment) {

    }
    public void cancelled(Appointment appointment){
        appointment.setAppointmentState(new CancelledState());
    }
}
