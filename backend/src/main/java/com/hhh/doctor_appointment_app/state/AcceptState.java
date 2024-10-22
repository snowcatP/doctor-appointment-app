package com.hhh.doctor_appointment_app.state;

import com.hhh.doctor_appointment_app.entity.Appointment;

import java.util.Date;

public class AcceptState implements AppointmentState{
    @Override
    public void next(Appointment appointment) {
        appointment.setAppointmentState(new CompletedState());
    }

    public void reschedule(Appointment appointment, Date newDateBooking){
        // Cập nhật lại ngày đặt lịch mới cho Appointment
        appointment.setDateBooking(newDateBooking);

        // Chuyển trạng thái sang RESCHEDULED
        appointment.setAppointmentState(new RescheduledState());
    }

    @Override
    public String getStatus() {
        return "Accept";
    }
}
