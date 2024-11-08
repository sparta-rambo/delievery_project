package com.delivery_project.entity;

import jakarta.persistence.*;

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

    @Column(nullable = false)
    private int totalPrice;

    @Column(nullable = false, length = 255)
    private String deliveryAddress;

}

