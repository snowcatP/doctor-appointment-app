package com.hhh.doctor_appointment_app.service.AppointmentService.Query;

import com.hhh.doctor_appointment_app.dto.mapper.DoctorMapper;
import com.hhh.doctor_appointment_app.dto.response.DoctorResponse.DoctorChatResponse;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.User;
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
public class GetAllDoctorsHaveBookedOfAPatient {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private FindUserByEmailQuery findUserByEmailQuery;
    @Autowired
    private DoctorMapper doctorMapper;
    @PreAuthorize("hasRole('PATIENT')")
    public List<DoctorChatResponse> getAllDoctorsHaveBookedOfAPatient(){
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = findUserByEmailQuery.findUserByEmail(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        List<Doctor> result = appointmentRepository.getDoctorsBookedByPatientEmail(user.getEmail());
        return doctorMapper.toListDoctorChatResponse(result);
    }
}
