package com.delivery_project.controller.api;

import com.delivery_project.dto.request.ReviewRequestDto;
import com.delivery_project.dto.response.MessageResponseDto;
import com.delivery_project.enums.SuccessMessage;
import com.delivery_project.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("{orderId}")
    public ResponseEntity<?> createReview(@PathVariable UUID orderId,
                                          @Valid @RequestBody ReviewRequestDto.Create reviewRequestDto) {
        reviewService.createReview(reviewRequestDto, orderId);
        return ResponseEntity.ok(new MessageResponseDto("Review"+ SuccessMessage.CREATE.getMessage()));
    }
}
