package com.delivery_project.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_orders")
public class Order {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(nullable = false, length = 20)
    private String orderType;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(nullable = false, precision = 10, scale = 2)
    private Double totalPrice;

    @Column(nullable = false, length = 255)
    private String deliveryAddress;

    private String deliveryRequest;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private LocalDateTime deletedAt;
    private String deletedBy;

    // Getters and Setters
}

