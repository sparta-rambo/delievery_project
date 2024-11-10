package com.delivery_project.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

public class OrderItemRequestDto {

    @Getter
    @Builder
    @NoArgsConstructor
    public static class Create{

        private UUID menuId;
        private int quantity;

        public Create(UUID menuId, int quantity) {
            this.menuId = menuId;
            this.quantity = quantity;
        }

    }
}
