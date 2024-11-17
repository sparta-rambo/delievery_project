package com.delivery_project.service;

import com.delivery_project.dto.request.ReviewRequestDto;
import com.delivery_project.entity.Order;
import com.delivery_project.entity.Review;
import com.delivery_project.entity.User;
import com.delivery_project.repository.jpa.OrderRepository;
import com.delivery_project.repository.jpa.ReviewRepository;
import com.delivery_project.repository.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;

    public void createReview(ReviewRequestDto.Create reviewRequestDto, UUID orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(()->new NullPointerException("존재하지 않는 주문입니다."));

        Review review = Review.builder()
                .id(UUID.randomUUID())
                .rating(reviewRequestDto.getRating())
                .comment(reviewRequestDto.getComment())
                .order(order)
                .build();

        reviewRepository.save(review);
    }
}
