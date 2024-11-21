package com.hhh.doctor_appointment_app.controller;

import com.hhh.doctor_appointment_app.dto.request.ChatMessageRequest.ChatMessageRequest;
import com.hhh.doctor_appointment_app.service.ChatService.Command.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class ChatController {
    @Autowired
    private ChatMessageService chatMessageService;

    @MessageMapping("/chat/{conversationId}")
    @MessageExceptionHandler(MessageConversionException.class)
    public void sendMessage(@DestinationVariable("conversationId") Long conversationId,
                                          ChatMessageRequest message) {
        chatMessageService.sendMessageToUser(conversationId, message);
    }
}
