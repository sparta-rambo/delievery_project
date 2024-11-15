package com.delivery_project.controller.api;

import com.delivery_project.common.utils.PageRequestUtils;
import com.delivery_project.dto.request.RestaurantRequestDto;
import com.delivery_project.dto.response.MessageResponseDto;
import com.delivery_project.dto.response.RestaurantResponseDto;
import com.delivery_project.entity.User;
import com.delivery_project.enums.SuccessMessage;
import com.delivery_project.enums.UserRoleEnum;
import com.delivery_project.security.UserDetailsImpl;
import com.delivery_project.service.RestaurantService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping()
    public ResponseEntity<?> createRestaurant(
      
        @Valid @RequestBody RestaurantRequestDto restaurantRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        restaurantService.createRestaurant(restaurantRequestDto, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new MessageResponseDto("Restaurant" + SuccessMessage.CREATE.getMessage()));
    }

    @PutMapping("/{restaurantId}")
    public ResponseEntity<?> updateRestaurant(
        @Valid @RequestBody RestaurantRequestDto restaurantRequestDto,
        @PathVariable UUID restaurantId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        restaurantService.updateRestaurant(restaurantRequestDto, restaurantId, userDetails.getUser());

        return ResponseEntity.ok(new MessageResponseDto("Restaurant" + SuccessMessage.UPDATE.getMessage()));
    }

    @PatchMapping("/{restaurantId}")
    public ResponseEntity<?> deleteRestaurant(@PathVariable UUID restaurantId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        restaurantService.deleteRestaurant(restaurantId, userDetails.getUser());

        return ResponseEntity.ok(new MessageResponseDto("Restaurant" + SuccessMessage.DELETE.getMessage()));
    }

    @GetMapping("/{restaurantId}")
    public RestaurantResponseDto getRestaurant(@PathVariable UUID restaurantId) {
        return restaurantService.getRestaurant(restaurantId);
    }

    @GetMapping()
    public Page<RestaurantResponseDto> getRestaurants(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "createdAt") String sortProperty,
        @RequestParam(defaultValue = "true") boolean ascending,
        @RequestParam(required = false) String search
    ) {
        PageRequest pageRequest = PageRequestUtils.getPageRequest(page, size, sortProperty, ascending);
        return restaurantService.getRestaurants(pageRequest, search);
    }

    @GetMapping("/category/{categoryId}")
    public Page<RestaurantResponseDto> getRestaurantsByCategory(
        @PathVariable UUID categoryId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "createdAt") String sortProperty,
        @RequestParam(defaultValue = "true") boolean ascending
    ) {
        PageRequest pageRequest = PageRequestUtils.getPageRequest(page, size, sortProperty, ascending);
        return restaurantService.getRestaurantsByCategory(pageRequest, categoryId);
    }

}
