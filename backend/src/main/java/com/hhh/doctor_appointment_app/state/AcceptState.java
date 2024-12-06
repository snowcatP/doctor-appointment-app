package com.hhh.doctor_appointment_app.state;

import com.hhh.doctor_appointment_app.entity.Appointment;

import java.util.Date;

public class AcceptState implements AppointmentState{
    @Override
    public void next(Appointment appointment) {
        appointment.setAppointmentState(new InProgessState());
    }

    public void reschedule(Appointment appointment){
        // Chuyển trạng thái sang RESCHEDULED
        appointment.setAppointmentState(new RescheduledState());
    }

    public void cancelled(Appointment appointment){
        appointment.setAppointmentState(new CancelledState());
    }

    @Override
    public String getStatus() {
        return "Accept";
    }
}
