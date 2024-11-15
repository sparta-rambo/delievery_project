package com.delivery_project.controller.api;

import com.delivery_project.entity.AIDescription;
import com.delivery_project.service.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;

    @PostMapping("/")
    public ResponseEntity<AIDescription> generateAIResponse(@RequestBody String prompt) {
        AIDescription aiDescription = aiService.generateResponse(prompt);
        return ResponseEntity.ok(aiDescription);
    }
}
