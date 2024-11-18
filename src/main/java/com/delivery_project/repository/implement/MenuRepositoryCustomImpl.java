package com.delivery_project.repository.implement;

import com.delivery_project.entity.Menu;
import com.delivery_project.entity.QMenu;
import com.delivery_project.entity.QRestaurant;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@Repository
public class MenuRepositoryCustomImpl implements MenuRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QMenu qMenu = QMenu.menu;
    private final QRestaurant qRestaurant = QRestaurant.restaurant;

    @PersistenceContext
    private EntityManager entityManager;

    public MenuRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Menu> findMenusWithRestaurant(BooleanExpression predicate, PageRequest pageRequest) {


        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(qMenu, pageRequest);

        return queryFactory
            .selectFrom(qMenu)
            .innerJoin(qMenu.restaurant, qRestaurant).fetchJoin()
            .where(predicate)
            .offset(pageRequest.getOffset())
            .limit(pageRequest.getPageSize())
            .orderBy(orderSpecifier)
            .fetch();
    }

    private OrderSpecifier getOrderSpecifier(QMenu qMenu, PageRequest pageRequest) {

        String sortProperty = pageRequest.getSort().getOrderFor("createdAt") != null ? "createdAt" : "updatedAt";
        boolean ascending = pageRequest.getSort().getOrderFor(sortProperty).getDirection().isAscending();

        if ("createdAt".equals(sortProperty)) {
            return ascending ? qMenu.createdAt.asc() : qMenu.createdAt.desc();
        } else if ("updatedAt".equals(sortProperty)) {
            return ascending ? qMenu.updatedAt.asc() : qMenu.updatedAt.desc();
        } else {
            return qMenu.createdAt.desc();
        }
    }
}
