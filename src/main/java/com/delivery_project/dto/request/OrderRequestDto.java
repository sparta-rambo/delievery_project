package com.delivery_project.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Builder
public class OrderRequestDto {


    @Getter
    @Builder
    @NoArgsConstructor
    public static class Create {

        private List<OrderItemRequestDto.Create> orderItemRequestDtos;
        private UUID restaurantId;
        private String orderType;
        private String deliveryAddress;
        private String deliveryRequest;

        public Create(List<OrderItemRequestDto.Create> orderItemRequestDtos, UUID restaurantId, String orderType, String deliveryAddress, String deliveryRequest) {
            this.orderItemRequestDtos = orderItemRequestDtos;
            this.restaurantId = restaurantId;
            this.orderType = orderType;
            this.deliveryAddress = deliveryAddress;
            this.deliveryRequest = deliveryRequest;
        }

    }
}
