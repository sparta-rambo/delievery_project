package com.delivery_project.repository.implement;

import com.delivery_project.entity.QRestaurant;
import com.delivery_project.entity.Restaurant;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@Repository
public class RestaurantRepositoryCustomImpl implements RestaurantRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @PersistenceContext
    private EntityManager entityManager;
    public RestaurantRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Restaurant> findRestaurants(BooleanExpression predicate, PageRequest pageRequest) {
        QRestaurant qRestaurant = QRestaurant.restaurant;

        // 정렬 기준 설정
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(qRestaurant, pageRequest);

        // Querydsl 쿼리 생성
        return queryFactory
            .selectFrom(qRestaurant)
            .leftJoin(QRestaurant.restaurant.category).fetchJoin()
            .leftJoin(QRestaurant.restaurant.owner).fetchJoin()
            .where(predicate)
            .offset(pageRequest.getOffset())
            .limit(pageRequest.getPageSize())
            .orderBy(orderSpecifier)
            .fetch();
    }

    private OrderSpecifier<?> getOrderSpecifier(QRestaurant qRestaurant, PageRequest pageRequest) {

        String sortProperty = pageRequest.getSort().getOrderFor("createdAt") != null ? "createdAt" : "updatedAt";
        boolean ascending = pageRequest.getSort().getOrderFor(sortProperty).getDirection().isAscending();

        // 정렬 기준 설정
        if ("createdAt".equals(sortProperty)) {
            return ascending ? qRestaurant.createdAt.asc() : qRestaurant.createdAt.desc();
        } else if ("updatedAt".equals(sortProperty)) {
            return ascending ? qRestaurant.updatedAt.asc() : qRestaurant.updatedAt.desc();
        } else {
            return qRestaurant.createdAt.desc();
        }
    }

}
