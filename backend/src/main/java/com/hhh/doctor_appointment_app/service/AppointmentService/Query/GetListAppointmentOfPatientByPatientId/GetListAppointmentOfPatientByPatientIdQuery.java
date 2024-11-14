package com.hhh.doctor_appointment_app.service.AppointmentService.Query.GetListAppointmentOfPatientByPatientId;

import com.hhh.doctor_appointment_app.dto.mapper.DoctorMapper;
import com.hhh.doctor_appointment_app.dto.response.AppointmentResponse.AppointmentResponse;
import com.hhh.doctor_appointment_app.dto.response.PageResponse;
import com.hhh.doctor_appointment_app.entity.Appointment;
import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.AppointmentRepository;
import com.hhh.doctor_appointment_app.service.UserService.Query.FindUserByEmail.FindUserByEmailQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetListAppointmentOfPatientByPatientIdQuery {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorMapper doctorMapper;

    public PageResponse<List<AppointmentResponse>> getListAppointmentOfPatientByPatientId(Long id,int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Appointment> appointmentPage = appointmentRepository.findByPatient_Id(id,pageable);

        //Convert entities to responses
        List<AppointmentResponse> appointmentResponses = appointmentPage.getContent().stream()
                .map(appointment -> {
                    AppointmentResponse response = new AppointmentResponse();
                    response.setId(appointment.getId());
                    response.setFullName(appointment.getFullName());
                    response.setPhone(appointment.getPhone());
                    response.setEmail(appointment.getEmail());
                    response.setReason(appointment.getReason());
                    response.setDateBooking(appointment.getDateBooking());
                    response.setBookingHour(appointment.getBookingHour());
                    response.setAppointmentStatus(appointment.getAppointmentStatus());
                    response.setDoctor(doctorMapper.toResponse(appointment.getDoctor()));
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
