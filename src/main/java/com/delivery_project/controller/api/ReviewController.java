package com.delivery_project.controller.api;

import com.delivery_project.common.utils.PageRequestUtils;
import com.delivery_project.dto.request.ReviewRequestDto;
import com.delivery_project.dto.response.MessageResponseDto;
import com.delivery_project.dto.response.OrderResponseDto;
import com.delivery_project.dto.response.ReviewResponseDto;
import com.delivery_project.enums.SuccessMessage;
import com.delivery_project.security.UserDetailsImpl;
import com.delivery_project.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("{reviewId}")
    public ResponseEntity<?> getReview(@PathVariable UUID reviewId) {
        ReviewResponseDto reviewResponseDto = reviewService.getReviewById(reviewId);
        return ResponseEntity.ok(reviewResponseDto);
    }

    @PatchMapping("{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable UUID reviewId,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        reviewService.deleteReviewById(reviewId,userDetails.getUser());
        return ResponseEntity.ok(new MessageResponseDto(SuccessMessage.DELETE.getMessage()));
    }

    @PutMapping("{reviewId}")
    public ResponseEntity<?> updateReview(@PathVariable UUID reviewId,
                                          @Valid @RequestBody ReviewRequestDto.Update reviewRequestDto) {
        reviewService.updateReview(reviewRequestDto, reviewId);
        return ResponseEntity.ok(new MessageResponseDto(SuccessMessage.UPDATE.getMessage()));
    }

    @GetMapping()
    public ResponseEntity<?> getAllReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortProperty,
            @RequestParam(defaultValue = "true") boolean ascending
    ) {
        PageRequest pageRequest = PageRequestUtils.getPageRequest(page, size, sortProperty, ascending);
        Page<ReviewResponseDto> reviews = reviewService.getAllReviews(pageRequest,sortProperty,ascending);
        return ResponseEntity.ok(reviews);
    }

}
