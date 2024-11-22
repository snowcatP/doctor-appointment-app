package com.hhh.doctor_appointment_app.service.ConversationService.Query;

import com.hhh.doctor_appointment_app.dto.mapper.ConversationMapper;
import com.hhh.doctor_appointment_app.dto.mapper.DoctorMapper;
import com.hhh.doctor_appointment_app.dto.mapper.PatientMapper;
import com.hhh.doctor_appointment_app.dto.response.ConversationResponse.ConversationResponse;
import com.hhh.doctor_appointment_app.entity.Conversation;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.entity.User;
import com.hhh.doctor_appointment_app.exception.NotFoundException;
import com.hhh.doctor_appointment_app.repository.ConversationRepository;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import com.hhh.doctor_appointment_app.repository.PatientRepository;
import com.hhh.doctor_appointment_app.service.UserService.Query.FindUserByEmail.FindUserByEmailQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllConversationsByUserQuery {
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private FindUserByEmailQuery findUserByEmailQuery;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private ConversationMapper conversationMapper;
    @Autowired
    private PatientMapper patientMapper;
    @Autowired
    private DoctorMapper doctorMapper;

    @PreAuthorize("hasAnyRole('DOCTOR', 'PATIENT')")
    public List<ConversationResponse> getAllConversationsByUser() {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = findUserByEmailQuery.findUserByEmail(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
        List<Conversation> conversations;
        if (user.getRole().getRoleName().name().equals("DOCTOR")) {
            Doctor doctor = doctorRepository.findDoctorByProfile_Email(username)
                    .orElseThrow(() -> new NotFoundException("Doctor not found"));
            conversations = conversationRepository.findAllByDoctorId(doctor.getId());
        } else {
            Patient patient = patientRepository.findPatientByProfile_Email(username)
                    .orElseThrow(() -> new NotFoundException("Patient not found"));
                conversations = conversationRepository.findAllByPatientId(patient.getId());
        }

        return conversations.stream().map(con -> ConversationResponse.builder()
                .id(con.getId())
                .doctor(doctorMapper.toChatResponse(con.getDoctor()))
                .patient(patientMapper.toChatResponse(con.getPatient()))
                .build()).toList();
    }
}
