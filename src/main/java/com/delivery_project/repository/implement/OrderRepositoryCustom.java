package com.delivery_project.repository.implement;

import com.querydsl.core.Tuple;

import java.util.List;
import java.util.UUID;

public interface OrderRepositoryCustom {

    List<Tuple> findOrderDetailsTuples(UUID orderId);
}
