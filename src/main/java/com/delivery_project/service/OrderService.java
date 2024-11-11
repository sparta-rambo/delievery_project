package com.delivery_project.service;

import com.delivery_project.common.exception.ResourceNotFoundException;
import com.delivery_project.dto.request.OrderItemRequestDto;
import com.delivery_project.dto.request.OrderRequestDto;
import com.delivery_project.entity.Menu;
import com.delivery_project.entity.Order;
import com.delivery_project.entity.Restaurant;
import com.delivery_project.entity.User;
import com.delivery_project.repository.jpa.MenuRepository;
import com.delivery_project.repository.jpa.OrderRepository;
import com.delivery_project.repository.jpa.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    public void createOrder(OrderRequestDto.Create orderRequestDto, User user) {
        Restaurant restaurant = restaurantRepository.findById(orderRequestDto.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        Order order = Order.builder()
                .orderRequestDto(orderRequestDto)
                .totalPrice(getOrderPrice(orderRequestDto))
                .user(user)
                .restaurant(restaurant)
                .build();

        orderRepository.save(order);
    }

    public int getOrderPrice(OrderRequestDto.Create orderRequestDto) {
        // 메뉴 ID 목록 추출 후, DB에서 한 번에 조회
        List<UUID> menuIds = orderRequestDto.getOrderItemRequestDtos().stream()
                .map(OrderItemRequestDto.Create::getMenuId)
                .collect(Collectors.toList());

        // 메뉴 목록을 Map 형태로 변환하여 조회 성능 개선
        Map<UUID, Menu> menuMap = menuRepository.findAllById(menuIds).stream()
                .collect(Collectors.toMap(Menu::getId, menu -> menu));

        // 가격 계산
        return orderRequestDto.getOrderItemRequestDtos().stream()
                .mapToInt(orderItemRequestDto -> {
                    Menu menu = menuMap.get(orderItemRequestDto.getMenuId());
                    if (menu == null) {
                        throw new ResourceNotFoundException("메뉴 데이터가 없습니다 : " + orderItemRequestDto.getMenuId());
                    }
                    return orderItemRequestDto.getQuantity() * menu.getPrice();
                })
                .sum();
    }


}
