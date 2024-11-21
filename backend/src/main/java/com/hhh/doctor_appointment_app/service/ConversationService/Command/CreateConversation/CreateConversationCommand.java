package com.hhh.doctor_appointment_app.service.ConversationService.Command.CreateConversation;

import com.hhh.doctor_appointment_app.dto.mapper.ConversationMapper;
import com.hhh.doctor_appointment_app.dto.request.ConversasionRequest.CreateConversationRequest;
import com.hhh.doctor_appointment_app.dto.response.ConversationResponse.ConversationResponse;
import com.hhh.doctor_appointment_app.entity.Conversation;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.repository.ConversationRepository;
import com.hhh.doctor_appointment_app.repository.DoctorRepository;
import com.hhh.doctor_appointment_app.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class CreateConversationCommand {
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ConversationMapper conversationMapper;

    @PreAuthorize("hasAnyRole('DOCTOR', 'PATIENT')")
    public ConversationResponse createConversation(CreateConversationRequest request) {
        Patient patient = patientRepository.findPatientByProfile_Email(request.getPatientEmail())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        Doctor doctor = doctorRepository.findDoctorByProfile_Email(request.getDoctorEmail())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        var conversation = conversationRepository
                .findByDoctorIdAndPatientId(doctor.getId(), patient.getId());
        if (conversation != null) return conversationMapper.toResponse(conversation);
        var res = conversationRepository.save(
                Conversation.builder()
                    .doctor(doctor)
                    .patient(patient)
                    .build());
        return conversationMapper.toResponse(res);
    }
}
