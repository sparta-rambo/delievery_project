package com.delivery_project.repository.implement;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryRepositoryCustomImpl {

    private final JPAQueryFactory queryFactory;

    @PersistenceContext
    private EntityManager entityManager;

    public CategoryRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }


//    @Override
//    public Optional<TestEntity> fetchByEntityId(Integer id) {
//        QTestEntity testEntity = QTestEntity.testEntity;
//        return Optional.ofNullable(queryFactory.selectFrom(testEntity)
//            .where(testEntity.id.eq(id))
//            .fetchOne());
//    }

}
