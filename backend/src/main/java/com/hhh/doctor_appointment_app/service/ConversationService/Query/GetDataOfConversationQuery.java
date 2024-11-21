package com.hhh.doctor_appointment_app.service.ConversationService.Query;

import com.hhh.doctor_appointment_app.dto.mapper.ChatMessageMapper;
import com.hhh.doctor_appointment_app.dto.response.ChatResponse.ChatMessageResponse;
import com.hhh.doctor_appointment_app.entity.Conversation;
import com.hhh.doctor_appointment_app.repository.ConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetDataOfConversationQuery {
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private ChatMessageMapper chatMessageMapper;

    public List<ChatMessageResponse> getDataOfConversation(Long conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("conversation not found"));
        return chatMessageMapper
                .toChatMessageResponseList(conversation.getChatMessages());
    }
}
