package com.delivery_project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_payments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
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

}

