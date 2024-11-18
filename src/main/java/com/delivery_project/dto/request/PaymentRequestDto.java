package com.delivery_project.dto.request;

import com.delivery_project.entity.Order;
import lombok.Getter;

public class PaymentRequestDto {

    @Getter
    public static class Create {
        private final int amount;
        private final String paymentMethod;
        private final String pgResponse;

        public Create(int amount, String paymentMethod, String pgResponse) {
            this.amount = amount;
            this.paymentMethod = paymentMethod;
            this.pgResponse = pgResponse;
        }
    }
}
