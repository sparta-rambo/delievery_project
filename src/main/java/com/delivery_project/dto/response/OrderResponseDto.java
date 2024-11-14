package com.delivery_project.dto.response;

import com.delivery_project.dto.MenuDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.Map;

@Getter
@AllArgsConstructor
public class OrderResponseDto {

    private Map<String, MenuDetails> orderItems;
    private String restaurantName;
    private String userName;
    private int totalPrice;
    private Timestamp orderDate;
    private String orderType;
    private String deliveryAddress;
    private String deliveryRequest;
    private String status;
}
