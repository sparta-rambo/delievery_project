package com.delivery_project.service;

import com.delivery_project.common.exception.BadRequestException;
import com.delivery_project.common.exception.ResourceNotFoundException;
import com.delivery_project.dto.request.MenuRequestDto;
import com.delivery_project.dto.response.MenuResponseDto;
import com.delivery_project.entity.Menu;
import com.delivery_project.entity.QMenu;
import com.delivery_project.entity.Restaurant;
import com.delivery_project.entity.User;
import com.delivery_project.enums.UserRoleEnum;
import com.delivery_project.repository.jpa.MenuRepository;
import com.delivery_project.repository.jpa.RestaurantRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.validation.Valid;
import java.util.List;
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
public class MenuService {

    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    private final QMenu qMenu = QMenu.menu;

    private Restaurant findRestaurantByIdOrThrow(UUID restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 가게입니다."));

        if (restaurant.getIsHidden()) {
            throw new ResourceNotFoundException("존재하지 않는 가게입니다.");
        }

        return restaurant;
    }

    private void validateUserAccess(User user, UUID ownerId) {
        if (!(user.getId().equals(ownerId) || user.getRole().equals(UserRoleEnum.MANAGER) || user.getRole()
            .equals(UserRoleEnum.MASTER))) {
            throw new BadRequestException("해당 가게에 대한 접근권한이 없습니다.");
        }
    }

    private Menu findMenuByIdOrThrow(UUID menuId) {
        Menu menu = menuRepository.findById(menuId)
            .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 메뉴입니다."));

        if (menu.getIsHidden()) {
            throw new ResourceNotFoundException("존재하지 않는 메뉴입니다.");
        }

        return menu;
    }

    public void createMenu(MenuRequestDto menuRequestDto, User user) {

        Restaurant restaurant = findRestaurantByIdOrThrow(menuRequestDto.getRestaurantId());
        validateUserAccess(user, restaurant.getOwner().getId());

        Menu menu = Menu.builder()
            .id(UUID.randomUUID())
            .restaurant(restaurant)
            .name(menuRequestDto.getName())
            .description(menuRequestDto.getDescription())
            .price(menuRequestDto.getPrice())
            .isHidden(false)
            .build();

        menuRepository.save(menu);
    }

    public void updateMenu(UUID menuId, @Valid MenuRequestDto menuRequestDto, User user) {

        Restaurant restaurant = findRestaurantByIdOrThrow(menuRequestDto.getRestaurantId());
        validateUserAccess(user, restaurant.getOwner().getId());

        Menu menu = findMenuByIdOrThrow(menuId);

        menuRepository.save(
            Menu.builder()
                .id(menuId)
                .restaurant(restaurant)
                .name(menuRequestDto.getName())
                .description(menuRequestDto.getDescription())
                .price(menuRequestDto.getPrice())
                .isHidden(false)
                .build()
        );
    }

    public void deleteMenu(UUID menuId, User user) {

        Menu menu = findMenuByIdOrThrow(menuId);
        Restaurant restaurant = findRestaurantByIdOrThrow(menu.getRestaurant().getId());

        validateUserAccess(user, restaurant.getOwner().getId());

        menu.markAsDeleted(user.getUsername());

        menuRepository.save(menu);
    }

    public Page<MenuResponseDto> getMenusByRestaurant(UUID restaurantId, PageRequest pageRequest) {

        Restaurant restaurant = findRestaurantByIdOrThrow(restaurantId);

        BooleanExpression predicate = qMenu.isHidden.isFalse().and(qMenu.restaurant.id.eq(restaurantId));

        List<Menu> menus = menuRepository.findMenusWithRestaurant(predicate, pageRequest);

        List<MenuResponseDto> menuResponseDtos = menus.stream()
            .map(menu -> MenuResponseDto.builder()
                .id(menu.getId())
                .restaurantId(menu.getRestaurant().getId())
                .name(menu.getName())
                .description(menu.getDescription())
                .price(menu.getPrice())
                .build())
            .toList();

        return new PageImpl<>(menuResponseDtos, pageRequest, menuResponseDtos.size());
    }

    public Page<MenuResponseDto> getMenusBySearch(String search, PageRequest pageRequest) {

        BooleanExpression predicate = qMenu.isHidden.isFalse().and(qMenu.name.containsIgnoreCase(search));

        List<Menu> menus = menuRepository.findMenusWithRestaurant(predicate, pageRequest);

        List<MenuResponseDto> menuResponseDtos = menus.stream()
            .map(menu -> MenuResponseDto.builder()
                .id(menu.getId())
                .restaurantId(menu.getRestaurant().getId())
                .name(menu.getName())
                .description(menu.getDescription())
                .price(menu.getPrice())
                .build())
            .toList();

        return new PageImpl<>(menuResponseDtos, pageRequest, menuResponseDtos.size());
    }
}
