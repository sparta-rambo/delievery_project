package com.delivery_project.repository.implement;

import com.delivery_project.entity.QTestEntity;
import com.delivery_project.entity.TestEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public class TestRepositoryCustomImpl implements TestRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @PersistenceContext
    private EntityManager entityManager;

    public TestRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }


    @Override
    public Optional<TestEntity> fetchByEntityId(Integer id) {
        QTestEntity testEntity = QTestEntity.testEntity;
        return Optional.ofNullable(queryFactory.selectFrom(testEntity)
                .where(testEntity.id.eq(id))
                .fetchOne());
    }

}
