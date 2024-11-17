package com.hhh.doctor_appointment_app.service.AppointmentService.Query.GetListAppointmentsForRescheduling;

import com.hhh.doctor_appointment_app.dto.mapper.AppointmentMapper;
import com.hhh.doctor_appointment_app.dto.request.AppointmentRequest.GetAppointmentForReschedulingRequest;
import com.hhh.doctor_appointment_app.dto.response.AppointmentResponse.AppointmentBookedResponse;
import com.hhh.doctor_appointment_app.entity.Appointment;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.AppointmentRepository;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import com.hhh.doctor_appointment_app.service.UserService.Query.FindUserByEmail.FindUserByEmailQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class GetListAppointmentsForReschedulingByPatientQuery {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    private FindUserByEmailQuery findUserByEmailQuery;

    @Autowired
    private DoctorRepository doctorRepository;

    @PreAuthorize("hasRole('PATIENT')")
    public List<AppointmentBookedResponse> getListAppointmentsForReschedulingByPatient(GetAppointmentForReschedulingRequest request) {
        User user = findUserByEmailQuery.findUserByEmail(request.getDoctorEmail())
                .orElseThrow(() -> new NotFoundException("Doctor not found"));

        Doctor doctor = doctorRepository.findDoctorByProfile_Email(request.getDoctorEmail())
                .orElseThrow(() -> new NotFoundException("Doctor Not Found"));
        LocalDateTime currentDateTime = LocalDateTime.now().plusDays(-1);
        LocalDateTime endDateTime = LocalDateTime.now().plusDays(14);
        Date currentDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());

        List<Appointment> result = appointmentRepository.getAppointmentsForBooking(doctor.getId(), currentDate, endDate);
        return appointmentMapper.toBookedResponses(result);
    }
}
