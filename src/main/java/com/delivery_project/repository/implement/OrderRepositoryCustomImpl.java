package com.delivery_project.repository.implement;

import com.delivery_project.entity.*;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<Tuple> findOrderDetailsTuples(UUID orderId) {
        QOrder o = QOrder.order;
        QUser u = QUser.user;
        QRestaurant r = QRestaurant.restaurant;
        QOrderItem oi = QOrderItem.orderItem;
        QMenu m = QMenu.menu;

        return queryFactory
                .select(
                        u.username,
                        r.name,
                        o.orderType,
                        o.status,
                        o.createdAt,
                        o.totalPrice,
                        o.deliveryAddress,
                        o.deliveryRequest,
                        oi.quantity,
                        m.name,
                        m.price
                )
                .from(oi)
                .join(oi.order, o)
                .join(o.user, u)
                .join(o.restaurant, r)
                .join(oi.menu, m)
                .where(o.id.eq(orderId))
                .fetch();
    }

    @Override
    public Page<Tuple> findAllOrderDetails(BooleanExpression predicate, PageRequest pageRequest) {
        QOrder o = QOrder.order;
        QUser u = QUser.user;
        QRestaurant r = QRestaurant.restaurant;
        QOrderItem oi = QOrderItem.orderItem;
        QMenu m = QMenu.menu;

        List<Tuple> results = queryFactory
                .select(
                        u.username,
                        r.name,
                        o.orderType,
                        o.status,
                        o.createdAt,
                        o.totalPrice,
                        o.deliveryAddress,
                        o.deliveryRequest,
                        oi.quantity,
                        m.name,
                        m.price
                )
                .from(oi)
                .join(oi.order, o)
                .join(o.user, u)
                .join(o.restaurant, r)
                .join(oi.menu, m)
                .where(predicate)
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(oi)
                .join(oi.order, o)
                .where(predicate)
                .fetchCount();

        return new PageImpl<>(results, pageRequest, total);
    }
}
