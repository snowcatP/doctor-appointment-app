package com.hhh.doctor_appointment_app.service.DoctorService.Query.GetAllPatientOfDoctor;

import com.hhh.doctor_appointment_app.dto.mapper.AppointmentMapper;
import com.hhh.doctor_appointment_app.dto.mapper.DoctorMapper;
import com.hhh.doctor_appointment_app.dto.response.AppointmentResponse.AppointmentResponse;
import com.hhh.doctor_appointment_app.dto.response.PageResponse;
import com.hhh.doctor_appointment_app.dto.response.PatientResponse.PatientResponse;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GetAllPatientOfDoctorQuery {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private FindUserByEmailQuery findUserByEmailQuery;

    @PreAuthorize("hasRole('DOCTOR')")
    public PageResponse<List<PatientResponse>> getAllPatientsOfDoctor(String patientName,int page, int size) {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = findUserByEmailQuery.findUserByEmail(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Doctor doctor = doctorRepository.findDoctorByProfile_Email(username).orElseThrow(() -> new NotFoundException("Doctor Not Found"));

        Pageable pageable = PageRequest.of(page-1, size);
        Page<Appointment> appointmentPage = appointmentRepository.findByDoctorEmailAndPatientNameContainingIgnoreCase(username,patientName,pageable);

        // Remove duplicate patients by using a set of patient IDs
        Set<Long> patientIds = new HashSet<>();
        //Convert entities to responses
        List<PatientResponse> appointmentResponses = appointmentPage.getContent().stream()
                .filter(appointment -> {
                    // Allow appointments with patients or guests
                    return appointment.getPatient() == null || patientIds.add(appointment.getPatient().getId());
                })
                .map(appointment -> {
                    PatientResponse response = new PatientResponse();
                    if (appointment.getPatient() != null) {
                        // Case: Patient exists
                        response.setId(appointment.getPatient().getId());
                        response.setFullname(appointment.getPatient().getProfile().getFullName());
                        response.setAddress(appointment.getPatient().getProfile().getAddress());
                        response.setDateOfBirth(appointment.getPatient().getProfile().getDateOfBirth());
                    } else {
                        // Case: Guest
                        response.setId(null); // Guest has no patient ID
                        response.setFullname(appointment.getFullName());
                        response.setEmail(appointment.getEmail());
                    }

                    response.setPhone(appointment.getPhone());
                    return response;
                })
                .collect(Collectors.toList());

        //Create PageResponse Object
        PageResponse<List<PatientResponse>> pageResponse = new PageResponse<>();
        pageResponse.ok(appointmentResponses);
        double total = Math.ceil((double) appointmentPage.getTotalElements() / size);
        pageResponse.setTotalPage((int) total);

        return pageResponse;
    }
}
