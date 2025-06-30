package com.fitness.aiService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service


public class GeminiService {

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final WebClient webClient;

    @Autowired
    public GeminiService(WebClient.Builder webClentBuilder){
        this.webClient=webClentBuilder.build();
    }

    public String getAnswer(String question){
        Map<String,Object> requestBody=Map.of(
                "contents",new Object[]{
                        Map.of(
                                "parts",new Object[]{
                                        Map.of("text",question)
                                }
                        )
                }
        );
        String response=webClient.post()
                .uri(geminiApiUrl+geminiApiKey)
                .header("Content-Type","application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return response;

    }
}
