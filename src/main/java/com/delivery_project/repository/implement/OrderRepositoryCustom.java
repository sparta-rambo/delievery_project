package com.delivery_project.repository.implement;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface OrderRepositoryCustom {

    List<Tuple> findOrderDetailsTuples(UUID orderId);

    Page<Tuple> findAllOrderDetails(BooleanExpression predicate, Pageable pageable);
}
