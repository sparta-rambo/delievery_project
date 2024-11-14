package com.delivery_project.repository.implement;

import com.delivery_project.config.QuerydslConfig;
import com.delivery_project.repository.jpa.OrderRepository;
import com.querydsl.core.Tuple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(QuerydslConfig.class)
class OrderRepositoryCustomImplTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void findOrderDetailsTuples() {
        //given
        UUID orderId = UUID.fromString("54661171-6767-433e-bb0f-d5295056872c");

        List<Tuple> orderDetailsTuples = orderRepository.findOrderDetailsTuples(orderId);
        orderDetailsTuples.forEach(orderDetailsTuple -> {
            System.out.println(orderDetailsTuple.toString());
        });

    }
}