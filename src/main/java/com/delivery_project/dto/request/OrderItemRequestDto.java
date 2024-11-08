package com.delivery_project.dto.request;

import lombok.Getter;

import java.util.UUID;

public class OrderItemRequestDto {

    @Getter
    public static class Create{

        private final UUID menuId;
        private final int quantity;

        public Create(UUID menuId, int quantity) {
            this.menuId = menuId;
            this.quantity = quantity;
        }

    }
}
