package com.garv.student_collaboration.service;

import com.garv.student_collaboration.dto.OllamaChatRequest;
import com.garv.student_collaboration.dto.OllamaChatResponse;
import com.garv.student_collaboration.dto.OllamaMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OllamaClientService {
    private final RestClient ollamaRestClient;
    @Value("${ollama.model}")
    private String model;
    public String askOllama(String prompt){
        OllamaMessage message = new OllamaMessage("user",prompt);
        OllamaChatRequest request = new OllamaChatRequest(model, List.of(message),false);
        OllamaChatResponse response = ollamaRestClient.post().uri("/api/chat").body(request).retrieve().body(OllamaChatResponse.class);
        return  response.getMessage().getContent();
    }
}
