package com.delivery_project.dto.request;

import jakarta.validation.constraints.NotNull;
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

        @NotNull(message = "주문 항목이 존재하지 않습니다.")
        private List<OrderItemRequestDto.Create> orderItemRequestDtos;

        @NotNull(message = "식당 id가 존재하지 않습니다.")
        private UUID restaurantId;

        @NotNull(message = "주문 방식이 존재하지 않습니다.")
        private String orderType;

        @NotNull(message = "배송지가 존재하지 않습니다.")
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
