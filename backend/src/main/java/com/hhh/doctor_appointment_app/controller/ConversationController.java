package com.hhh.doctor_appointment_app.controller;

import com.hhh.doctor_appointment_app.dto.request.ConversasionRequest.CreateConversationRequest;
import com.hhh.doctor_appointment_app.service.ConversationService.Command.CreateConversation.CreateConversationCommand;
import com.hhh.doctor_appointment_app.service.ConversationService.Query.GetAllConversationsByUserQuery;
import com.hhh.doctor_appointment_app.service.ConversationService.Query.GetDataOfConversationQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("api/conversation")
public class ConversationController {

    @Autowired
    private CreateConversationCommand createConversationCommand;

    @Autowired
    private GetAllConversationsByUserQuery getAllConversationsByUserQuery;

    @Autowired
    private GetDataOfConversationQuery getDataOfConversationQuery;

    @PostMapping("/create")
    public ResponseEntity<?> createNewConversation(@RequestBody CreateConversationRequest request) {
        try {
            return ResponseEntity.ok(createConversationCommand.createConversation(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get-all-by-user")
    public ResponseEntity<?> getAllConversations() {
        try {
            return ResponseEntity.ok(getAllConversationsByUserQuery.getAllConversationsByUser());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get-data-of-conversation/{conversationId}")
    public ResponseEntity<?> getConversationDataOfConversation(@PathVariable("conversationId") Long conversationId) {
        try {
            return ResponseEntity.ok(getDataOfConversationQuery
                    .getDataOfConversation(conversationId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
