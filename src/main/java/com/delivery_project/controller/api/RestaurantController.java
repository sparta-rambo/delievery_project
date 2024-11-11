package com.delivery_project.controller.api;

import com.delivery_project.common.exception.ResourceNotFoundException;
import com.delivery_project.dto.request.RestaurantRequestDto;
import com.delivery_project.dto.response.MessageResponseDto;
import com.delivery_project.dto.response.RestaurantResponseDto;
import com.delivery_project.entity.Category;
import com.delivery_project.entity.User;
import com.delivery_project.enums.SuccessMessage;
import com.delivery_project.service.RestaurantService;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping()
    public ResponseEntity<?> createRestaurant(
        @RequestBody RestaurantRequestDto restaurantRequestDto) {
        // 임시 user
        User user = new User(
            UUID.fromString("12345678-afc5-4164-a7b4-0be4fa6281ed"),
            "testUser",
            "password123",
            "ROLE_OWNER",
            "1234 Test St, Test City"
        );

        restaurantService.createRestaurant(restaurantRequestDto, user);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new MessageResponseDto("Restaurant" + SuccessMessage.CREATE.getMessage()));
    }

    @PutMapping("/{restaurantId}")
    public ResponseEntity<?> updateRestaurant(@RequestBody RestaurantRequestDto restaurantRequestDto, @PathVariable UUID restaurantId) {
        // 임시 user
        User user = new User(
            UUID.fromString("12345678-afc5-4164-a7b4-0be4fa6281ed"),
            "testUser",
            "password123",
            "ROLE_OWNER",
            "1234 Test St, Test City"
        );

        restaurantService.updateRestaurant(restaurantRequestDto, restaurantId, user);

        return ResponseEntity.ok(new MessageResponseDto("Restaurant" + SuccessMessage.UPDATE.getMessage()));
    }

    @PatchMapping("/{restaurantId}")
    public ResponseEntity<?> deleteRestaurant(@PathVariable UUID restaurantId) {
        // 임시 user
        User user = new User(
            UUID.fromString("12345678-afc5-4164-a7b4-0be4fa6281ed"),
            "testUser",
            "password123",
            "ROLE_OWNER",
            "1234 Test St, Test City"
        );

        restaurantService.deleteRestaurant(restaurantId, user);

        return ResponseEntity.ok(new MessageResponseDto("Restaurant" + SuccessMessage.DELETE.getMessage()));
    }

    @GetMapping("/{restaurantId}")
    public RestaurantResponseDto getRestaurant(@PathVariable UUID restaurantId) {
        return restaurantService.getRestaurant(restaurantId);
    }
}
