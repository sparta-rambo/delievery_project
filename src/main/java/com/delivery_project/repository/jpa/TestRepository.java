package com.delivery_project.repository.jpa;

import com.delivery_project.entity.TestEntity;
import com.delivery_project.repository.implement.TestRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<TestEntity, Integer>, TestRepositoryCustom {
}
