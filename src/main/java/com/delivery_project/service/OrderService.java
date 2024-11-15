package com.delivery_project.service;

import com.delivery_project.common.exception.ResourceNotFoundException;
import com.delivery_project.dto.MenuDetails;
import com.delivery_project.dto.request.OrderItemRequestDto;
import com.delivery_project.dto.request.OrderRequestDto;
import com.delivery_project.dto.response.OrderResponseDto;
import com.delivery_project.entity.*;
import com.delivery_project.repository.jpa.MenuRepository;
import com.delivery_project.repository.jpa.OrderRepository;
import com.delivery_project.repository.jpa.RestaurantRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.HashMap;
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

    public Order getOrder(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("주문을 찾을 수 없습니다."));
    }

    public void deleteOrder(Order order, User user) {
        order.delete(user.getUsername());
        orderRepository.save(order);
    }

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
        List<UUID> menuIds = getMenuIdByDtoCreate(orderRequestDto);
        Map<UUID, Menu> menuMap = getMenuMapByIds(menuIds);

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

    public Map<UUID, Menu> getMenuMapByIds(List<UUID> menuIds) {
        return menuRepository.findAllById(menuIds).stream()
                .collect(Collectors.toMap(Menu::getId, menu -> menu));
    }

    public List<UUID> getMenuIdByDtoCreate(OrderRequestDto.Create orderRequestDto) {
        return orderRequestDto.getOrderItemRequestDtos().stream()
                .map(OrderItemRequestDto.Create::getMenuId)
                .collect(Collectors.toList());
    }

    public OrderResponseDto findOrderDetails(UUID orderId) {
        List<Tuple> results = orderRepository.findOrderDetailsTuples(orderId);
        return convertToOrderResponseDto(results);
    }

    public OrderResponseDto convertToOrderResponseDto(List<Tuple> results) {
        OrderResponseDto orderResponseDto = results.stream()
                .findFirst()  // 첫 번째 Tuple에서 기본 주문 정보만 가져오기
                .map(tuple -> new OrderResponseDto(
                        new HashMap<>(), // 변경 가능한 빈 Map 생성
                        tuple.get(QRestaurant.restaurant.name),
                        tuple.get(QUser.user.username),
                        tuple.get(QOrder.order.totalPrice),
                        Timestamp.valueOf(tuple.get(QOrder.order.createdAt)),
                        tuple.get(QOrder.order.orderType),
                        tuple.get(QOrder.order.deliveryAddress),
                        tuple.get(QOrder.order.deliveryRequest),
                        tuple.get(QOrder.order.status)
                ))
                .orElseThrow(() -> new IllegalArgumentException("Order not found "));

        // 각 메뉴 정보를 Map에 추가
        results.forEach(tuple -> {
            String menuName = tuple.get(QMenu.menu.name);
            MenuDetails menuDetails = new MenuDetails(
                    tuple.get(QOrderItem.orderItem.quantity),
                    tuple.get(QMenu.menu.price)
            );
            orderResponseDto.getOrderItems().put(menuName, menuDetails);  // OrderItems Map에 추가
        });
        return orderResponseDto;
    }

    public Page<OrderResponseDto> getAllOrderDetails(PageRequest pageRequest, String username, String restaurantName, String orderType, String status) {
        BooleanExpression predicate = createPredicate(username, restaurantName, orderType, status);
        Page<Tuple> tuplePage = orderRepository.findAllOrderDetails(predicate, pageRequest);
        List<OrderResponseDto> orderResponseDtos = tuplePage.getContent().stream()
                .map(tuple -> convertToOrderResponseDto(List.of(tuple)))
                .collect(Collectors.toList());

        // `Page<OrderResponseDto>` 생성 및 반환
        return new PageImpl<>(orderResponseDtos, pageRequest, tuplePage.getTotalElements());
    }

    private BooleanExpression createPredicate(String username, String restaurantName, String orderType, String status) {
        QOrder o = QOrder.order;
        QUser u = QUser.user;
        QRestaurant r = QRestaurant.restaurant;

        BooleanExpression predicate = Expressions.asBoolean(true).isTrue();

        if (username != null) {
            predicate = predicate.and(u.username.eq(username));
        }
        if (restaurantName != null) {
            predicate = predicate.and(r.name.eq(restaurantName));
        }
        if (orderType != null) {
            predicate = predicate.and(o.orderType.eq(orderType));
        }
        if (status != null) {
            predicate = predicate.and(o.status.eq(status));
        }

        return predicate;
    }


}
