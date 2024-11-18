package com.delivery_project.repository.implement;

import com.delivery_project.dto.response.PaymentResponseDto;
import com.delivery_project.entity.QPayment;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.delivery_project.entity.QPayment.payment;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryCustomImpl implements PaymentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<PaymentResponseDto> findPaymentResponseDtoByPaymentId(UUID paymentId) {
        QPayment qPayment = payment;

        return Optional.ofNullable(queryFactory
                .select(Projections.fields(
                        PaymentResponseDto.class,
                        qPayment.createdAt,
                        qPayment.createdBy,
                        qPayment.amount,
                        qPayment.paymentMethod
                ))
                .from(qPayment)
                .where(qPayment.id.eq(paymentId))
                .fetchOne());
    }

    @Override
    public Page<PaymentResponseDto> findAllPayments(BooleanExpression predicate, PageRequest pageRequest) {
        QPayment qPayment = QPayment.payment;

        // 데이터 페이징 조회
        List<PaymentResponseDto> content = queryFactory
                .select(Projections.constructor(PaymentResponseDto.class,
                        qPayment.createdAt,
                        qPayment.createdBy,
                        qPayment.amount,
                        qPayment.paymentMethod
                ))
                .from(qPayment)
                .where(predicate)
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        // 전체 데이터 수 조회
        long total = queryFactory
                .select(qPayment.count())
                .from(qPayment)
                .where(predicate)
                .fetchOne();

        // PageImpl로 결과 반환
        return new PageImpl<>(content, pageRequest, total);
    }
}

