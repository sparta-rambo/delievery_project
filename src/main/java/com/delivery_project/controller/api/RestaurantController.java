package com.delivery_project.controller.api;

import com.delivery_project.dto.request.RestaurantRequestDto;
import com.delivery_project.dto.response.MessageResponseDto;
import com.delivery_project.entity.User;
import com.delivery_project.enums.SuccessMessage;
import com.delivery_project.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
        //security
        User user = new User();

        restaurantService.createRestaurant(restaurantRequestDto, user);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new MessageResponseDto("Restaurant" + SuccessMessage.CREATE.getMessage()));
    }
}
