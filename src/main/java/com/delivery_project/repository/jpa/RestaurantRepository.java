package com.delivery_project.repository.jpa;

import com.delivery_project.entity.Restaurant;
import com.delivery_project.repository.implement.RestaurantRepositoryCustom;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, UUID>,
    RestaurantRepositoryCustom {
}
