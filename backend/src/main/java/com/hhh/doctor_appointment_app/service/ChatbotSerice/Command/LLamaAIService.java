package com.hhh.doctor_appointment_app.service.ChatbotSerice.Command;

import com.hhh.doctor_appointment_app.dto.request.ChatbotRequest.ChatbotRequest;
import com.hhh.doctor_appointment_app.dto.response.ChatbotResponse.ChatbotResponse;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi.*;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.ai.chat.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LLamaAIService {

    private final OllamaChatModel chatModel;

    @Autowired
    public LLamaAIService(OllamaChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Value("${spring.ai.ollama.chat.options.model}")
    private String llamaModel;

    @Value("classpath:/prompts/SystemPrompt.st")
    private Resource systemPrompt;

    public ChatbotResponse generateResponse(ChatbotRequest request) {

        PromptTemplate promptTemplate = new PromptTemplate(systemPrompt);
        Prompt prompt = promptTemplate.create(
                Map.of("prompt", request.getPrompt()),
                OllamaOptions.create()
                    .withModel(OllamaModel.LLAMA3_2)
                    .withTemperature(0.8));

        ChatResponse response = chatModel.call(prompt);

        return ChatbotResponse.builder()
                    .result(response.getResult().getOutput().getContent())
                    .sender("BOT")
                    .build();
    }
}
