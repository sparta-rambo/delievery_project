package com.delivery_project.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.delivery_project.common.exception.BadRequestException;
import com.delivery_project.common.exception.ResourceNotFoundException;
import com.delivery_project.dto.request.RestaurantRequestDto;
import com.delivery_project.entity.Category;
import com.delivery_project.entity.Restaurant;
import com.delivery_project.entity.User;
import com.delivery_project.enums.UserRoleEnum;
import com.delivery_project.repository.jpa.CategoryRepository;
import com.delivery_project.repository.jpa.RestaurantRepository;
import com.delivery_project.repository.jpa.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RestaurantServiceTest {

    @InjectMocks
    private RestaurantService restaurantService;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    private User testManager;
    private Category testCategory;
    private User testOwner;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        testManager = User.builder()
            .id(UUID.randomUUID())
            .username("manager")
            .role(UserRoleEnum.MANAGER)
            .build();

        testCategory = Category.builder()
            .id(UUID.randomUUID())
            .name("한식")
            .build();

        testOwner = User.builder()
            .id(UUID.randomUUID())
            .username("owner")
            .role(UserRoleEnum.OWNER)
            .build();
    }

    @Test
    @DisplayName("가게 생성 성공 테스트")
    void createRestaurantSuccessTest() {
        // given
        RestaurantRequestDto requestDto = RestaurantRequestDto.builder()
            .name("김밥천국")
            .address("서울시")
            .categoryId(testCategory.getId())
            .ownerId(testOwner.getId())
            .build();

        when(categoryRepository.findById(any(UUID.class))).thenReturn(Optional.of(testCategory));
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(testOwner));

        // when
        restaurantService.createRestaurant(requestDto, testManager);

        // then
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
    }

    @Test
    @DisplayName("카테고리 찾기 실패 테스트")
    void createRestaurantCategoryNotFoundTest() {
        // given
        RestaurantRequestDto requestDto = RestaurantRequestDto.builder()
            .name("김밥천국")
            .address("서울시")
            .categoryId(UUID.randomUUID())
            .ownerId(testOwner.getId())
            .build();

        when(categoryRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // then
        assertThrows(ResourceNotFoundException.class,
            () -> restaurantService.createRestaurant(requestDto, testManager));
    }

    @Test
    @DisplayName("권한 없는 유저가 가게 생성 시도 테스트")
    void createRestaurantUnauthorizedUserTest() {
        // given
        User unauthorizedUser = User.builder()
            .id(UUID.randomUUID())
            .username("unauthorized")
            .role(UserRoleEnum.CUSTOMER)
            .build();

        RestaurantRequestDto requestDto = RestaurantRequestDto.builder()
            .name("김밥천국")
            .address("서울시")
            .categoryId(testCategory.getId())
            .ownerId(testOwner.getId())
            .build();

        // then
        assertThrows(BadRequestException.class,
            () -> restaurantService.createRestaurant(requestDto, unauthorizedUser));
    }
}
