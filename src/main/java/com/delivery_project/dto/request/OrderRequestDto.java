package com.delivery_project.dto.request;

import lombok.Getter;

import java.util.List;
import java.util.UUID;


public class OrderRequestDto {

    @Getter
    public static class Create {

        private final List<OrderItemRequestDto.Create> orderItemRequestDtos;
        private final UUID restaurantId;
        private final String orderType;
        private final String deliveryAddress;
        private final String deliveryRequest;

        public Create(List<OrderItemRequestDto.Create> orderItemRequestDtos, UUID restaurantId, String orderType, String deliveryAddress, String deliveryRequest) {
            this.orderItemRequestDtos = orderItemRequestDtos;
            this.restaurantId = restaurantId;
            this.orderType = orderType;
            this.deliveryAddress = deliveryAddress;
            this.deliveryRequest = deliveryRequest;
        }

    }
}
