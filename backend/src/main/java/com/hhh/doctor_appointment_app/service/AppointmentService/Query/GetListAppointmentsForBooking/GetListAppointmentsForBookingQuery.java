package com.hhh.doctor_appointment_app.service.AppointmentService.Query.GetListAppointmentsForBooking;

import com.hhh.doctor_appointment_app.dto.mapper.AppointmentMapper;
import com.hhh.doctor_appointment_app.dto.response.AppointmentResponse.AppointmentBookedResponse;
import com.hhh.doctor_appointment_app.entity.Appointment;
import com.hhh.doctor_appointment_app.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class GetListAppointmentsForBookingQuery {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private AppointmentMapper appointmentMapper;

    public List<AppointmentBookedResponse> getListAppointmentsForBooking(Long doctorId) {
        LocalDateTime currentDateTime = LocalDateTime.now().plusDays(-1);
        LocalDateTime endDateTime = LocalDateTime.now().plusDays(14);
        Date currentDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());

        List<Appointment> result = appointmentRepository.getAppointmentsForBooking(doctorId, currentDate, endDate);
        return appointmentMapper.toBookedResponses(result);
    }
}
