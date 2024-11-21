package com.hhh.doctor_appointment_app.service.ChatService.Command;

import com.hhh.doctor_appointment_app.dto.mapper.ChatMessageMapper;
import com.hhh.doctor_appointment_app.dto.mapper.ConversationMapper;
import com.hhh.doctor_appointment_app.dto.request.ChatMessageRequest.ChatMessageRequest;
import com.hhh.doctor_appointment_app.dto.response.ChatResponse.ChatMessageResponse;
import com.hhh.doctor_appointment_app.entity.ChatMessage;
import com.hhh.doctor_appointment_app.entity.Conversation;
import com.hhh.doctor_appointment_app.entity.Doctor;
import com.hhh.doctor_appointment_app.entity.Patient;
import com.hhh.doctor_appointment_app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class ChatMessageService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private ChatMessageMapper chatMessageMapper;
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private ConversationMapper conversationMapper;

    public void sendMessageToUser(Long conversationId,
                                  ChatMessageRequest message) {
        Doctor doctor = doctorRepository.findByProfile_Email(message.getDoctorEmail())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        Patient patient = patientRepository.findPatientByProfile_Email(message.getPatientEmail())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        Optional<Conversation> conv = conversationRepository
                    .findById(message.getConversationId());
        Conversation conversation = conv.orElseGet(() -> Conversation.builder()
                .patient(patient)
                .doctor(doctor)
                .createdAt(new Date())
                .build());
        ChatMessage chatMessage =
                chatMessageRepository.save(
                ChatMessage.builder()
                        .message(message.getMessage())
                        .timeStamp(new Date())
                        .sender(message.getSender())
                        .conversation(conversation)
                        .build()
        );
        messagingTemplate.convertAndSend(
                "/user/chat/conversation/" + conversationId,
                chatMessageMapper.toChatMessageResponse(chatMessage)
        );
    }
}
