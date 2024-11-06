package com.delivery_project.repository;

import com.delivery_project.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<TestEntity, Integer> {
}
