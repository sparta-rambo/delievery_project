package com.delivery_project.service;

import com.delivery_project.entity.Order;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @BeforeAll
    public static void setUp() {

        Order order = new Order();
    }

    @Test
    void create() {
    }

    @Test
    void getOrderPrice() {
    }
}