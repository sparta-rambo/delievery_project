package com.delivery_project.service;

import com.delivery_project.entity.AIDescription;
import com.delivery_project.repository.jpa.AIDescriptionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AIService {
    @Value("${google.api.key}")
    private String apiKey;

    private final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent";

    private final AIDescriptionRepository aiDescriptionRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AIDescription generateResponse(String prompt) {
        String modifiedPrompt = prompt + " 답변을 50자 이하로 작성해 주세요.";

        // JSON 요청 형식 설정 (curl 명령어 형식에 맞추기)
        String requestBody = "{ \"contents\": [ { \"parts\": [ { \"text\": \"" + modifiedPrompt.replace("\"", "\\\"") + "\" } ] } ] }";

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // API 요청 수행
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                API_URL + "?key=" + apiKey, HttpMethod.POST, request, String.class
        );

        // AI 응답에서 텍스트만 추출
        String aiResponse = response.getBody();
        JsonNode rootNode = null;

        // JsonProcessingException 예외 처리
        try {
            rootNode = objectMapper.readTree(aiResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

        JsonNode candidates = rootNode.path("candidates");
        StringBuilder formattedResponse = new StringBuilder();

        if (candidates.isArray() && candidates.size() > 0) {
            JsonNode candidate = candidates.get(0);
            JsonNode content = candidate.path("content");
            if (content.has("parts")) {
                for (JsonNode part : content.path("parts")) {
                    String text = part.path("text").asText();
                    text = text.replaceAll("\\\\\n", " ");
                    formattedResponse.append(text).append(" ");
                }
            }
        }

        System.out.println("AI Response: \n" + formattedResponse.toString());

        AIDescription aiDescription = AIDescription.builder()
                .id(UUID.randomUUID())
                .aiRequest(modifiedPrompt)
                .aiResponse(formattedResponse.toString())
                .build();

        return aiDescriptionRepository.save(aiDescription);
    }
}
