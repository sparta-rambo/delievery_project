package com.delivery_project.service;

import com.delivery_project.common.exception.BadRequestException;
import com.delivery_project.common.exception.ResourceNotFoundException;
import com.delivery_project.dto.request.RestaurantRequestDto;
import com.delivery_project.dto.response.RestaurantResponseDto;
import com.delivery_project.entity.Category;
import com.delivery_project.entity.Restaurant;
import com.delivery_project.entity.User;
import com.delivery_project.repository.jpa.CategoryRepository;
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
    private final CategoryRepository categoryRepository;

    private Restaurant findRestaurantByIdOrThrow(UUID restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 가게입니다."));

        if (restaurant.getIsHidden()) {
            throw new ResourceNotFoundException("존재하지 않는 가게입니다.");
        }

        return restaurant;
    }

    private Category findCategoryByIdOrThrow(UUID categoryId) {
        return categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("유효하지 않은 카테고리입니다."));
    }

    // 권한 검증
    private void validateUserAccess(User user, UUID ownerId) {
        if (!(user.getId().equals(ownerId) || user.getRole().equals("ROLE_MASTER") || user.getRole()
            .equals("ROLE_MANAGER"))) {
            throw new BadRequestException("해당 가게에 대한 접근권한이 없습니다.");
        }
    }

    public void createRestaurant(RestaurantRequestDto restaurantRequestDto, User user) {
        System.out.println(user.getRole());
        if (!(user.getRole().equals("ROLE_MASTER")
            || user.getRole().equals("ROLE_MANAGER"))) {
            throw new BadRequestException("접근권한이 없습니다.");
        }

        Category category = findCategoryByIdOrThrow(restaurantRequestDto.getCategoryId());

        // spring security
//        User owner = userRepository.findById(restaurantRequestDto.getOwnerId())
//                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 유저입니다."));

        // 임시 owner
        User owner = new User(
            UUID.fromString("12345678-afc5-4164-a7b4-0be4fa6281ed"),
            "testUser",
            "password123",
            "ROLE_OWNER",
            "1234 Test St, Test City"
        );

        Restaurant restaurant = Restaurant.builder()
            .id(UUID.randomUUID())
            .name(restaurantRequestDto.getName())
            .category(category)
            .owner(owner)
            .address(restaurantRequestDto.getAddress())
            .isHidden(restaurantRequestDto.getIsHidden())
            .build();

        restaurantRepository.save(restaurant);
    }

    public void updateRestaurant(RestaurantRequestDto restaurantRequestDto, UUID restaurantId,
        User user) {
        Restaurant restaurant = findRestaurantByIdOrThrow(restaurantId);

        validateUserAccess(user, restaurant.getOwner().getId());

        Category category = findCategoryByIdOrThrow(restaurantRequestDto.getCategoryId());

        restaurantRepository.save(
            Restaurant.builder()
                .id(restaurantId)
                .name(restaurantRequestDto.getName())
                .category(category)
                .owner(user)
                .address(restaurantRequestDto.getAddress())
                .isHidden(restaurantRequestDto.getIsHidden())
                .build()
        );
    }

    public void deleteRestaurant(UUID restaurantId, User user) {
        Restaurant restaurant = findRestaurantByIdOrThrow(restaurantId);

        validateUserAccess(user, restaurant.getOwner().getId());

        restaurantRepository.save(
            Restaurant.builder()
                .id(restaurantId)
                .name(restaurant.getName())
                .category(restaurant.getCategory())
                .owner(user)
                .address(restaurant.getAddress())
                .isHidden(true)
                .build()
        );
    }

    public RestaurantResponseDto getRestaurant(UUID restaurantId) {
        Restaurant restaurant = findRestaurantByIdOrThrow(restaurantId);

        return RestaurantResponseDto.builder()
            .id(restaurantId)
            .name(restaurant.getName())
            .categoryId(restaurant.getCategory().getId())
            .ownerId(restaurant.getOwner().getId())
            .address(restaurant.getAddress())
            .isHidden(restaurant.getIsHidden())
            .build();
    }
}
