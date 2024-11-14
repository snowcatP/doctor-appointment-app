package com.hhh.doctor_appointment_app.service.AppointmentService.Query.GetAllAppointmentsByDoctorId;

import com.hhh.doctor_appointment_app.dto.mapper.AppointmentMapper;
import com.hhh.doctor_appointment_app.dto.response.AppointmentResponse.AppointmentResponse;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.AppointmentRepository;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import com.hhh.doctor_appointment_app.service.UserService.Query.FindUserByEmail.FindUserByEmailQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllAppointmentsByDoctorIdQuery {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    private FindUserByEmailQuery findUserByEmailQuery;

    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public List<AppointmentResponse> getAllAppointmentsByDoctorId() {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        findUserByEmailQuery.findUserByEmail(username)
                .orElseThrow(() -> new NotFoundException("Doctor not found"));

        Doctor doctor = doctorRepository.findByProfile_Email(username)
                .orElseThrow(() -> new NotFoundException("Doctor not found"));

        return appointmentMapper
                .toResponses(appointmentRepository.getAppointmentsByDoctor(doctor));
    }
}
