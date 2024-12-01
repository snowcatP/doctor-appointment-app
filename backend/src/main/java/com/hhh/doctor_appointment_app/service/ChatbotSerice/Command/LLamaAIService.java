package com.hhh.doctor_appointment_app.service.ChatbotSerice.Command;

import com.hhh.doctor_appointment_app.dto.request.ChatbotRequest.ChatbotRequest;
import com.hhh.doctor_appointment_app.dto.response.ChatbotResponse.ChatbotResponse;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi.*;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ai.chat.model.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class LLamaAIService {

    private final OllamaChatModel chatModel;

    @Autowired
    public LLamaAIService(OllamaChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Value("${spring.ai.ollama.chat.options.model}")
    private String llamaModel;

    public List<ChatbotResponse> generateResponse(ChatbotRequest request) {
        ChatResponse response = chatModel.call(
                new Prompt(
                        request.getPrompt(),
                        OllamaOptions.create()
                                .withModel(llamaModel)
                )
        );

        List<ChatbotResponse> responses = new ArrayList<>();
        response.getResults().forEach(res ->
            responses.add(ChatbotResponse.builder()
                    .result(res.getOutput().getContent())
                    .sender("BOT")
                    .build()));
        return responses;
    }
}
