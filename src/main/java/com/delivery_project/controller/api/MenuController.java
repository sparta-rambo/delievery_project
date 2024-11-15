package com.delivery_project.controller.api;

import com.delivery_project.common.utils.PageRequestUtils;
import com.delivery_project.dto.request.MenuRequestDto;
import com.delivery_project.dto.response.MenuResponseDto;
import com.delivery_project.dto.response.MessageResponseDto;
import com.delivery_project.entity.User;
import com.delivery_project.enums.SuccessMessage;
import com.delivery_project.enums.UserRoleEnum;
import com.delivery_project.security.UserDetailsImpl;
import com.delivery_project.service.MenuService;
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
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @PostMapping()
    public ResponseEntity<?> createMenu(
        @Valid @RequestBody MenuRequestDto menuRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        menuService.createMenu(menuRequestDto, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new MessageResponseDto("Menu" + SuccessMessage.CREATE.getMessage()));
    }

    @PutMapping("/{menuId}")
    public ResponseEntity<?> updateMenu(
        @PathVariable UUID menuId,
        @Valid @RequestBody MenuRequestDto menuRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        menuService.updateMenu(menuId, menuRequestDto, userDetails.getUser());

        return ResponseEntity.ok(new MessageResponseDto("Menu" + SuccessMessage.UPDATE.getMessage()));
    }

    @PatchMapping("/{menuId}")
    public ResponseEntity<?> deleteMenu(
        @PathVariable UUID menuId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        menuService.deleteMenu(menuId, userDetails.getUser());

        return ResponseEntity.ok(new MessageResponseDto("Menu" + SuccessMessage.DELETE.getMessage()));
    }

    @GetMapping("/{restaurantId}")
    public Page<MenuResponseDto> getMenusByRestaurant(
        @PathVariable UUID restaurantId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "createdAt") String sortProperty,
        @RequestParam(defaultValue = "true") boolean ascending
    ) {
        PageRequest pageRequest = PageRequestUtils.getPageRequest(page, size, sortProperty, ascending);

        return menuService.getMenusByRestaurant(restaurantId, pageRequest);
    }

    @GetMapping()
    public Page<MenuResponseDto> getMenusBySearch(
        @RequestParam String search,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "createdAt") String sortProperty,
        @RequestParam(defaultValue = "true") boolean ascending
    ) {
        PageRequest pageRequest = PageRequestUtils.getPageRequest(page, size, sortProperty, ascending);

        return menuService.getMenusBySearch(search, pageRequest);
    }
}
