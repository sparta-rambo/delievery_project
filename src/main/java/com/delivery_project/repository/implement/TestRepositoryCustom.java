package com.delivery_project.repository.implement;

import com.delivery_project.entity.TestEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestRepositoryCustom {

    Optional<TestEntity> fetchByEntityId(Integer id);

}
