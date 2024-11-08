package com.delivery_project.repository.interfaces;

import com.delivery_project.entity.TestEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestRepositoryCustom {

    Optional<TestEntity> asd(Integer id);

//    void addTestEntity(TestEntity testEntity);
}
