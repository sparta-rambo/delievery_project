package com.delivery_project.dto.response;

import com.delivery_project.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {

    private LocalDateTime createdAt;
    private String createdBy;
    private int amount;
    private String paymentMethod;

    public PaymentResponseDto(Payment payment) {
        this.createdAt = payment.getCreatedAt();
        this.createdBy = payment.getCreatedBy();
        this.amount = payment.getAmount();
        this.paymentMethod = payment.getPaymentMethod();
    }
}


