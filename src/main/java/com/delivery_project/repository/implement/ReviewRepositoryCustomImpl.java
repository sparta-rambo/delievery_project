package com.delivery_project.repository.implement;

import com.delivery_project.dto.response.ReviewResponseDto;
import com.delivery_project.entity.QReview;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<ReviewResponseDto> getReviews(OrderSpecifier<?> orderSpecifier, PageRequest pageRequest) {
        QReview qReview = QReview.review;

        // 페이징 데이터 조회
        List<ReviewResponseDto> content = queryFactory
                .select(Projections.constructor(ReviewResponseDto.class,
                        qReview.rating,
                        qReview.comment))
                .from(qReview)
                .orderBy(orderSpecifier) // 동적 정렬 적용
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        // 전체 데이터 개수 조회
        long total = queryFactory
                .select(qReview.count())
                .from(qReview)
                .fetchOne();

        // PageImpl 생성 및 반환
        return new PageImpl<>(content, pageRequest, total);
    }

}
