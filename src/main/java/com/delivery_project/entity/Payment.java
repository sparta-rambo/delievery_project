package com.delivery_project.entity;

import com.delivery_project.dto.request.PaymentRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "p_payments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Payment extends Timestamped {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false, length = 50)
    private String paymentMethod;

    @Builder
    public Payment(PaymentRequestDto.Create paymentRequestDto, Order order) {
        this.id = UUID.randomUUID();
        this.order = order;
        this.amount = paymentRequestDto.getAmount();
        this.paymentMethod = paymentRequestDto.getPaymentMethod();
    }

}

