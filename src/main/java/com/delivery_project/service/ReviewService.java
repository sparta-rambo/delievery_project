package com.delivery_project.service;

import com.delivery_project.dto.request.ReviewRequestDto;
import com.delivery_project.dto.response.ReviewResponseDto;
import com.delivery_project.entity.Order;
import com.delivery_project.entity.QReview;
import com.delivery_project.entity.Review;
import com.delivery_project.entity.User;
import com.delivery_project.repository.jpa.OrderRepository;
import com.delivery_project.repository.jpa.ReviewRepository;
import com.querydsl.core.types.OrderSpecifier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;

    public void createReview(ReviewRequestDto.Create reviewRequestDto, UUID orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NullPointerException("존재하지 않는 주문입니다."));

        Review review = Review.builder()
                .id(UUID.randomUUID())
                .rating(reviewRequestDto.getRating())
                .comment(reviewRequestDto.getComment())
                .order(order)
                .build();

        reviewRepository.save(review);
    }

    public ReviewResponseDto getReviewById(UUID reviewId) {
        return new ReviewResponseDto(reviewRepository.findById(reviewId).orElseThrow(() -> new NullPointerException("존재하지 않는 리뷰입니다.")));
    }

    public void deleteReviewById(UUID reviewId, User user) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new NullPointerException("존재하지 않는 리뷰입니다."));
        review.delete(user.getUsername());
        reviewRepository.save(review);
    }

    public void updateReview(ReviewRequestDto.Update reviewRequestDto, UUID reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new NullPointerException("존재하지 않는 리뷰입니다."));
        review.update(reviewRequestDto.getRating(), reviewRequestDto.getComment());
        reviewRepository.save(review);
    }


    private OrderSpecifier<?> createOrderSpecifier(String sortProperty, boolean ascending) {
        QReview qReview = QReview.review;

        switch (sortProperty) {
            case "rating":
                return ascending ? qReview.rating.asc() : qReview.rating.desc();
            case "createdAt":
                return ascending ? qReview.createdAt.asc() : qReview.createdAt.desc();
            default:
                throw new IllegalArgumentException("Invalid sort property: " + sortProperty);
        }
    }

    public Page<ReviewResponseDto> getAllReviews(PageRequest pageRequest, String sortProperty, Boolean ascending) {
        OrderSpecifier<?> orderSpecifier = createOrderSpecifier(sortProperty, ascending);
        return reviewRepository.getReviews(orderSpecifier, pageRequest);
    }
}
