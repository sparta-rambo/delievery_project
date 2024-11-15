package com.delivery_project.repository.implement;

import com.delivery_project.entity.Restaurant;
import com.delivery_project.entity.TestEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepositoryCustom {
    List<Restaurant> findRestaurants(BooleanExpression predicate, PageRequest pageRequest);

    Double calculateAverageRating(UUID restaurantId);
}
