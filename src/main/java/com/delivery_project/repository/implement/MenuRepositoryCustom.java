package com.delivery_project.repository.implement;

import com.delivery_project.entity.Menu;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepositoryCustom {

    List<Menu> findMenusWithRestaurant(BooleanExpression predicate, PageRequest pageRequest);
}
