package com.hhh.doctor_appointment_app.service.AppointmentService.Query.GetAppointmentWithPage;

import com.hhh.doctor_appointment_app.dto.mapper.AppointmentMapper;
import com.hhh.doctor_appointment_app.dto.response.AppointmentResponse.AppointmentResponse;
import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorResponse;
import com.hhh.doctor_appointment_app.dto.response.PageResponse;
import com.hhh.doctor_appointment_app.entity.Appointment;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetAppointmentWithPageQuery {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentMapper appointmentMapper;

    public PageResponse<List<AppointmentResponse>> getAppointmentsWithPage(int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Appointment> appointmentPage = appointmentRepository.getAppointmentsWithPage(pageable);

        //Convert entities to responses
        List<AppointmentResponse> appointmentResponses = appointmentPage.getContent().stream()
                .map(appointment -> {
                    AppointmentResponse response = new AppointmentResponse();
                    response.setId(appointment.getId());
                    response.setFullname(appointment.getFullname());
                    response.setGender(appointment.isGender());
                    response.setPhone(appointment.getPhone());
                    response.setEmail(appointment.getEmail());
                    response.setDateOfBirth(appointment.getDateOfBirth());
                    response.setReason(appointment.getReason());
                    response.setDateBooking(appointment.getDateBooking());
                    response.setAppointmentStatus(appointment.getAppointmentStatus());
                    response.setDoctor(appointment.getDoctor().getProfile());
                    return response;
                })
                .collect(Collectors.toList());

        //Create PageResponse Object
        PageResponse<List<AppointmentResponse>> pageResponse = new PageResponse<>();
        pageResponse.ok(appointmentResponses);
        double total = Math.ceil((double) appointmentPage.getTotalElements() / size);
        pageResponse.setTotalPage((int) total);

        return pageResponse;
    }
}
