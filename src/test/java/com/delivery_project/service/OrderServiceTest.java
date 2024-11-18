package com.delivery_project.service;

import com.delivery_project.dto.response.OrderResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Test
    @DisplayName("주문 상세정보 조회 테스트")
    void findOrderDetails() {
        //given
        UUID orderId = UUID.fromString("54661171-6767-433e-bb0f-d5295056872c");

        //when
        OrderResponseDto orderDetails = orderService.findOrderDetails(orderId);

        //then
        orderDetails.getOrderItems().forEach((menuName, menuDetail) ->
                System.out.println("Menu Name: " + menuName + ", Quantity: " + menuDetail.getQuantity() + ", Price: " + menuDetail.getPrice())
        );

    }

    @Test
    void create() {
    }

    @Test
    void getOrderPrice() {
    }
}