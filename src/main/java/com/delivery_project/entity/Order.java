package com.delivery_project.entity;

import com.delivery_project.dto.request.OrderRequestDto;
import com.delivery_project.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "p_orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Order extends Timestamped{

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

    @Column(nullable = false, length = 255)
    private String deliveryRequest;

    @Builder
    public Order(OrderRequestDto.Create orderRequestDto,int totalPrice,User user,Restaurant restaurant){
        this.id = UUID.randomUUID();
        this.user = user;
        this.restaurant = restaurant;
        this.deliveryAddress = orderRequestDto.getDeliveryAddress();
        this.deliveryRequest = orderRequestDto.getDeliveryRequest();
        this.orderType = orderRequestDto.getOrderType();
        this.status = OrderStatus.CONFIRM.getStatus();
        this.totalPrice = totalPrice;
    }

}

