package com.delivery_project.service;

import com.delivery_project.common.exception.BadRequestException;
import com.delivery_project.common.exception.ResourceNotFoundException;
import com.delivery_project.dto.request.RestaurantRequestDto;
import com.delivery_project.dto.response.RestaurantResponseDto;
import com.delivery_project.entity.Category;
import com.delivery_project.entity.QRestaurant;
import com.delivery_project.entity.Restaurant;
import com.delivery_project.entity.User;
import com.delivery_project.enums.UserRoleEnum;
import com.delivery_project.repository.jpa.CategoryRepository;
import com.delivery_project.repository.jpa.RestaurantRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;
    private final QRestaurant qRestaurant = QRestaurant.restaurant;

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

        if (!(user.getRole().equals("ROLE_MASTER")
            || user.getRole().equals("ROLE_MANAGER"))) {
            throw new BadRequestException("접근권한이 없습니다.");
        }

        Category category = findCategoryByIdOrThrow(restaurantRequestDto.getCategoryId());

        // spring security
//        User owner = userRepository.findById(restaurantRequestDto.getOwnerId())
//                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 유저입니다."));

        // 임시 user
        User owner = new User(
            UUID.fromString("12345678-afc5-4164-a7b4-0be4fa6281ed"),
            "testUser",
            "password123",
            UserRoleEnum.OWNER,
            false
        );

        Restaurant restaurant = Restaurant.builder()
            .id(UUID.randomUUID())
            .name(restaurantRequestDto.getName())
            .category(category)
            .owner(owner)
            .address(restaurantRequestDto.getAddress())
            .isHidden(false)
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
                .isHidden(false)
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

    @Transactional(readOnly = true)
    public RestaurantResponseDto getRestaurant(UUID restaurantId) {
        Restaurant restaurant = findRestaurantByIdOrThrow(restaurantId);

        return RestaurantResponseDto.builder()
            .id(restaurantId)
            .name(restaurant.getName())
            .categoryId(restaurant.getCategory().getId())
            .ownerId(restaurant.getOwner().getId())
            .address(restaurant.getAddress())
            .averageRating(Optional.ofNullable(restaurant.getAverageRating()))
            .build();
    }

    @Transactional(readOnly = true)
    public Page<RestaurantResponseDto> getRestaurants(PageRequest pageRequest, String search) {
        // Querydsl로 조건을 생성 (숨겨진 가게 제외)
        BooleanExpression predicate = qRestaurant.isHidden.isFalse();

        // 검색 조건이 있는 경우
        if (search != null && !search.isEmpty()) {
            predicate = predicate.and(qRestaurant.name.containsIgnoreCase(search));
        }

        // Querydsl 페이징 적용
        List<Restaurant> restaurants = restaurantRepository.findRestaurants(predicate, pageRequest);

        // DTO로 변환
        List<RestaurantResponseDto> restaurantResponseDtos = restaurants.stream()
            .map(restaurant -> RestaurantResponseDto.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .categoryId(restaurant.getCategory().getId())
                .ownerId(restaurant.getOwner().getId())
                .address(restaurant.getAddress())
                .averageRating(Optional.ofNullable(restaurant.getAverageRating()))
                .build())
            .toList();

        return new PageImpl<>(restaurantResponseDtos, pageRequest, restaurants.size());
    }

    @Transactional(readOnly = true)
    public Page<RestaurantResponseDto> getRestaurantsByCategory(PageRequest pageRequest, UUID categoryId) {
        // Querydsl로 조건을 생성 (카테고리에 해당하는 가게 조회)
        BooleanExpression predicate = qRestaurant.category.id.eq(categoryId);

        // Querydsl 페이징 적용
        List<Restaurant> restaurants = restaurantRepository.findRestaurants(predicate, pageRequest);

        // DTO로 변환
        List<RestaurantResponseDto> restaurantResponseDtos = restaurants.stream()
            .map(restaurant -> RestaurantResponseDto.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .categoryId(restaurant.getCategory().getId())
                .ownerId(restaurant.getOwner().getId())
                .address(restaurant.getAddress())
                .averageRating(Optional.ofNullable(restaurant.getAverageRating()))
                .build())
            .toList();

        return new PageImpl<>(restaurantResponseDtos, pageRequest, restaurants.size());
    }
}
