package com.delivery_project.service;

import com.delivery_project.common.exception.ResourceNotFoundException;
import com.delivery_project.dto.request.RestaurantRequestDto;
import com.delivery_project.entity.Category;
import com.delivery_project.entity.Restaurant;
import com.delivery_project.entity.User;
import com.delivery_project.repository.jpa.RestaurantRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;


    public void createRestaurant(RestaurantRequestDto restaurantRequestDto, User user) {
//        Category category = categoryRepository.findById(restaurantRequestDto.getCategoryId()).
//            orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        // 임시 카테고리 데이터
        Category category = new Category();

        Restaurant restaurant = Restaurant.builder()
            .id(UUID.randomUUID())
            .name(restaurantRequestDto.getName())
            .category(category)
            .owner(user)
            .address(restaurantRequestDto.getAddress())
            .isHidden(restaurantRequestDto.getIsHidden())
            .build();

        restaurantRepository.save(restaurant);
    }
}
