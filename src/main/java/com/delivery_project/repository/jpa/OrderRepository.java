package com.delivery_project.repository.jpa;

import com.delivery_project.entity.Order;
import com.delivery_project.repository.implement.OrderRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID>, OrderRepositoryCustom {
}
