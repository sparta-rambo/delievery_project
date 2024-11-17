package com.delivery_project.repository.implement;

import com.delivery_project.dto.response.PaymentResponseDto;
import com.delivery_project.entity.QPayment;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
}
