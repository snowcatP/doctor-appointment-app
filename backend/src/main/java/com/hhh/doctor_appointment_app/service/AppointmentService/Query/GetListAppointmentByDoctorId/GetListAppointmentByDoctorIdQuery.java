package com.hhh.doctor_appointment_app.service.AppointmentService.Query.GetListAppointmentByDoctorId;

import com.hhh.doctor_appointment_app.dto.mapper.AppointmentMapper;
import com.hhh.doctor_appointment_app.dto.mapper.DoctorMapper;
import com.hhh.doctor_appointment_app.dto.mapper.MedicalRecordMapper;
import com.hhh.doctor_appointment_app.dto.mapper.PatientMapper;
import com.hhh.doctor_appointment_app.dto.response.AppointmentResponse.AppointmentResponse;
import com.hhh.doctor_appointment_app.dto.response.PageResponse;
import com.hhh.doctor_appointment_app.entity.Appointment;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.AppointmentRepository;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import com.hhh.doctor_appointment_app.service.UserService.Query.FindUserByEmail.FindUserByEmailQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetListAppointmentByDoctorIdQuery {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private FindUserByEmailQuery findUserByEmailQuery;

    @Autowired
    private MedicalRecordMapper medicalRecordMapper;

    @Autowired
    private PatientMapper patientMapper;

    @PreAuthorize("hasRole('DOCTOR')")
    public PageResponse<List<AppointmentResponse>> getAppointmentsWithPageByDoctorId(int page, int size) {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = findUserByEmailQuery.findUserByEmail(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Doctor doctor = doctorRepository.findDoctorByProfile_Email(username)
                .orElseThrow(() -> new NotFoundException("Doctor Not Found"));


        Pageable pageable = PageRequest.of(page-1, size, Sort.by("dateBooking").descending());
        Page<Appointment> appointmentPage = appointmentRepository.findByDoctorId(doctor.getId(),pageable);

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
                    response.setAppointmentStatus(appointment.getAppointmentStatus());
                    response.setCusType(appointment.getCusType());
                    response.setDoctor(doctorMapper.toResponse(appointment.getDoctor()));
                    response.setBookingHour(appointment.getBookingHour());
                    response.setMedicalRecordResponse(medicalRecordMapper.toResponse(appointment.getMedicalRecord()));
                    response.setPatientResponse(patientMapper.toResponse(appointment.getPatient()));
                    if(appointment.getPatient()!=null){
                        response.setAvatarFilePath(appointment.getPatient().getProfile().getAvatarFilePath());
                    }
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
