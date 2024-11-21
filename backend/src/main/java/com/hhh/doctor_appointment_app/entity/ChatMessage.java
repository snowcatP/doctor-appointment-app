package com.hhh.doctor_appointment_app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private Date timeStamp;
    private String sender;
    @ManyToOne()
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;
}
