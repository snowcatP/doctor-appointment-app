package com.hhh.doctor_appointment_app.controller;

import com.hhh.doctor_appointment_app.dto.request.ChatbotRequest.ChatbotRequest;
import com.hhh.doctor_appointment_app.service.ChatbotSerice.Command.LLamaAIService;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai/chatbot")
public class ChatbotController {
    @Autowired
    private LLamaAIService llamaAIService;

    @PostMapping()
    public ResponseEntity<?> generateText(@RequestBody ChatbotRequest request) {
        try {
            return ResponseEntity.ok(llamaAIService.generateResponse(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
