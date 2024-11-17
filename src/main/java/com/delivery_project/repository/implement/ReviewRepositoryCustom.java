package com.delivery_project.repository.implement;

import com.delivery_project.dto.response.ReviewResponseDto;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ReviewRepositoryCustom {

    Page<ReviewResponseDto> getReviews(OrderSpecifier<?> orderSpecifier, PageRequest pageRequest);
}
