package com.delivery_project.repository.implement;

import com.delivery_project.entity.QRestaurant;
import com.delivery_project.entity.QReview;
import com.delivery_project.entity.Restaurant;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
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
        QReview qReview = QReview.review;

        // 정렬 기준 설정
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(qRestaurant, pageRequest);

        // Querydsl 쿼리 생성
        return queryFactory
            .select(Projections.fields(
                Restaurant.class,
                qRestaurant.id.as("id"),
                qRestaurant.name.as("name"),
                qRestaurant.address.as("address"),
                qRestaurant.category.as("category"),
                qRestaurant.owner.as("owner"),
                qReview.rating.avg().as("averageRating")
            ))
            .from(qRestaurant)
            .leftJoin(qRestaurant.category)
            .leftJoin(qRestaurant.owner)
            .leftJoin(qReview).on(qReview.order.restaurant.id.eq(qRestaurant.id))
            .where(predicate)
            .groupBy(qRestaurant.id, qRestaurant.category.id, qRestaurant.owner.id)
            .offset(pageRequest.getOffset())
            .limit(pageRequest.getPageSize())
            .orderBy(orderSpecifier)
            .fetch()
            .stream()
            .map(restaurant -> {
                if (restaurant.getAverageRating() != null) {
                    double roundRating = new BigDecimal(restaurant.getAverageRating())
                        .setScale(1, RoundingMode.HALF_UP)
                        .doubleValue(); // 평점을 소수점 첫째 자리까지 반올림
                    restaurant.setAverageRating(roundRating);
                }
                return restaurant;
            })
            .collect(Collectors.toList());
    }

    public Double calculateAverageRating(UUID restaurantId) {
        QReview qReview = QReview.review;

        // 리뷰의 평균 평점을 구하는 쿼리
        Double averageRating = queryFactory
            .select(qReview.rating.avg())
            .from(qReview)
            .where(qReview.order.restaurant.id.eq(restaurantId))
            .fetchOne();

        if (averageRating != null) {
            // 평점을 소수점 첫째 자리까지 반올림
            return new BigDecimal(averageRating).setScale(1, RoundingMode.HALF_UP).doubleValue();
        } else {
            return null;  // 리뷰가 없으면 null 반환
        }
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
